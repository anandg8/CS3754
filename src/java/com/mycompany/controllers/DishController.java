package com.mycompany.controllers;

import com.mycompany.EntityBeans.Dish;
import com.mycompany.controllers.util.JsfUtil;
import com.mycompany.controllers.util.JsfUtil.PersistAction;
import com.mycompany.FacadeBeans.DishFacade;
import com.mycompany.managers.AccountManager;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
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
    
    /**
     * Return list of dish for past reservations 
     * 
     * @return list of dish
     */
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
    
    /**
     * Creates a dish
     */
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
    
    /**
     * Gets list of dish
     * @return list of dish
     */
    public List<Dish> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
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
    
    /**
     * Get number of available dish 
     * 
     * @return Number of available dish 
     */
    public int getAvailability() {
        if (selected == null || selected.getId() == null) 
        {
            return 0;
        }
        return (selected.getNumGuests() - getFacade().getCurrentNumReservations(selected.getId()));
    }
    
    /**
     * Calculates the distance between two points
     * longitude and latitude of each point
     * 
     * @param lat1  Latitude 1
     * @param lng1  Longitude 1
     * @param lat2  Latitude 2
     * @param lng2  Longitude 2
     * @return 
     */
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
    
    /**
     * Rounds the value passed in to 2 decimal places 
     * only if the value has more than 2 decimal places
     * 
     * @param val value to be rounded
     * @return Rounded value to decimal to places
     */
    double RoundTo2Decimals(double val) {
            DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
    }
    
    /**
     * Calculate distance between to location between
     * dishUser and account user
     * 
     * @param dishUserLocation      Location of user which includes both longitude and latitude separated by ','
     * @param accountUserLocation   Location of user which includes both longitude and latitude separated by ','
     * @return distance between two points
     */
    public double calculateDistance(String dishUserLocation, String accountUserLocation) {
        String[] dLocation = dishUserLocation.split(",");
        String[] aLocation = accountUserLocation.split(",");

        if (dLocation.length > 1 && aLocation.length > 1) {
            double result = distance(Double.parseDouble(dLocation[0]),
                    Double.parseDouble(dLocation[1]), 
                    Double.parseDouble(aLocation[0]), 
                    Double.parseDouble(aLocation[1]));
            result *= 0.62137119224;
            return RoundTo2Decimals(result);
        }
        else
            return -1;
    }
    
    /**
     * Checks if a user accessing dish data is guest or not
     * 
     * @return true if a user is a guest false if not 
     */
    public boolean isGuest() {
        if (selected == null || accountManager.getSelected() == null || selected.getId() == null) {
            return true;
        }
        return getFacade().isGuest(accountManager.getSelected().getId(), selected.getId());

    }
        
    /**
     * Returns Dish available
     * 
     * @return Dish available
     */
    public List<Dish> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }
    
    /**
     * Returns dish with ID
     * 
     * @param id ID of dish
     * @return Dish with ID
     */
    public Dish getDish(java.lang.Integer id) {
        return getFacade().find(id);
    }
    
    /**
     * Returns Dish Available
     * @return list of dish available
     */
    public List<Dish> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    /**
     * Reserves a dish selected
     */
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
    
    /**
     * Check if dish selected is not user's own dish
     * 
     * @return if dish selected is not user's own dish return true otherwise return false
     */
    public boolean isNotSelf() {
        if (selected != null && selected.getUserId() != null && accountManager != null && accountManager.getSelected() != null)
            return selected.getUserId().getId() != accountManager.getSelected().getId();
        return false;
    }
    
    /**
     * Directs to chat site
     * 
     * @return link to chat site
     */
    public String chatWith() {
        return "/Chat.xhtml?faces-redirect=true";
    }
}
