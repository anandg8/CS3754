package com.mycompany.controllers;

import com.mycompany.EntityBeans.Dish;
import com.mycompany.controllers.util.JsfUtil;
import com.mycompany.controllers.util.JsfUtil.PersistAction;
import com.mycompany.FacadeBeans.DishFacade;
import com.mycompany.managers.AccountManager;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("dishController")
@SessionScoped
public class DishController implements Serializable {

    @EJB
    private com.mycompany.FacadeBeans.DishFacade ejbFacade;
    private List<Dish> items = null;
    private Dish selected;


    @Inject
    AccountManager accountManager;
    
    public DishController() {
    }

    public Dish getSelected() {
        return selected;
    }

    public void setSelected(Dish selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DishFacade getFacade() {
        return ejbFacade;
    }
    
    public List<Dish> getCurrentReservations() {
        return getFacade().findCurrentReservationsByUser(accountManager.getSelected().getId());

    }
    
    public List<Dish> getPastReservations() {
        List<Dish> temp = getFacade().findPastReservationsByUser(accountManager.getSelected().getId());
        return temp;
    }
    
    public List<Dish> getCurrentUserDishes()
    {
        return getFacade().findCurrentUserDishes(accountManager.getSelected().getId());
    }
    
    public boolean hasEnoughCredits() {
        if (selected != null) {
            return selected.getCost() < accountManager.getNumberOfCreditsAvailable();
        }
        return false;
    }

    public String rateAndReview() {
        
        return "/reservations/RateForumBoard.xhtml?faces-redirect=true";
    }
    
    public Dish prepareCreate() {
        selected = new Dish();
        initializeEmbeddableKey();
        return selected;
    }
    

    public void create() {
        selected.setUserId(accountManager.getSelected());
        if (selected.getMealTime().before(selected.getReservationTime())) {
            JsfUtil.addErrorMessage("Please select the meal time after the reservation time!");
        } else {
            persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DishCreated"));
            if (!JsfUtil.isValidationFailed()) {
                items = null;    // Invalidate list of items to trigger re-query.
            }
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DishUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DishDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Dish> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
//    public List<Dish> findAll() {
//        List<Dish> dishes = (List<Dish>) 
//    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    if (persistAction == PersistAction.CREATE) {
                     getFacade().createDish(selected);
                    } else {
                        getFacade().edit(selected);
                    }
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
    public int getAvailability() {
        if (selected == null || selected.getId() == null) 
        {
            return 0;
        }
        return (selected.getNumGuests() - getFacade().getCurrentNumReservations(selected.getId()));
    }
    
    public static double distance(
            double lat1, double lng1, double lat2, double lng2) {
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
           Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) 
          * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;
        return d;
    }
    
    double RoundTo2Decimals(double val) {
            DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
    }
    public double calculateDistance(String dishUserLocation, String accountUserLocation) {
        String[] dLocation = dishUserLocation.split(",");
        String[] aLocation = accountUserLocation.split(",");
        // 0 Lat 1 Long
        if (dLocation.length > 1 && aLocation.length > 1) {
            double l1 = Double.parseDouble(dLocation[0]);
            double result = distance(l1, 
                    Double.parseDouble(dLocation[1]), 
                    Double.parseDouble(aLocation[0]), 
                    Double.parseDouble(aLocation[1]));
            result *= 0.62137119224;
            return RoundTo2Decimals(result);
        }
        else
            return -1;
    }
    
    public boolean isGuest() {
        if (selected == null || accountManager.getSelected() == null || selected.getId() == null) {
            return true;
        }
        return getFacade().isGuest(accountManager.getSelected().getId(), selected.getId());

    }

    public Dish getDish(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Dish> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Dish> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public void reserveDish() {
        getFacade().reserveDish(selected.getId(), accountManager.getSelected().getId());
        JsfUtil.addSuccessMessage("Dish has been reserved!");
        accountManager.creditTransferFromTo(accountManager.getSelected().getId(), selected.getUserId().getId(), (int)selected.getCost());
    }
    
    public void unreserveDish() {
        getFacade().unreserveDish(selected.getId(), accountManager.getSelected().getId());
        JsfUtil.addSuccessMessage("Refunding your money...");
        accountManager.creditTransferFromTo(selected.getUserId().getId(), accountManager.getSelected().getId(), (int)selected.getCost());
    }

    int getCommentsConjunctionID() {
        return getFacade().getCommentsConjunctionID(selected.getId());
    }

    @FacesConverter(forClass = Dish.class)
    public static class DishControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DishController controller = (DishController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "dishController");
            return controller.getDish(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Dish) {
                Dish o = (Dish) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Dish.class.getName()});
                return null;
            }
        }

    }
    
    public boolean isNotSelf() {
        if (selected != null && selected.getUserId() != null && accountManager != null && accountManager.getSelected() != null)
            return selected.getUserId().getId() != accountManager.getSelected().getId();
        return false;
    }
    
    public String chatWith() {
        return "/Chat.xhtml?faces-redirect=true";
    }

}
