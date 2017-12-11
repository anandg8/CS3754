package com.mycompany.controllers;

import com.mycompany.EntityBeans.Dish;
import com.mycompany.controllers.util.JsfUtil;
import com.mycompany.controllers.util.JsfUtil.PersistAction;
import com.mycompany.FacadeBeans.DishFacade;
import com.mycompany.managers.AccountManager;

import java.io.Serializable;
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

    public Dish prepareCreate() {
        selected = new Dish();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        selected.setUserId(accountManager.getSelected());
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DishCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
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

    public Dish getDish(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Dish> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Dish> getItemsAvailableSelectOne() {
        return getFacade().findAll();
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
    
    public String generateTable() {
        String html = "<main><table><thead><tr><th>Picture</th><th>Description</th><th>Cuisine</th><th>Cook</th><th>Cost</th><th>Reservation Time</th><th>Meal Time</th></tr></thead><tbody>";
        for (Dish d : getItems()) {
           html += "<tr>";
           html += "<td data-title='Picture'>";
           html += "<img src='" + d.getDishPicturePath() + "' style='width:300px'/></td>";
           html += "<td data-title='Description'>" + d.getDescription() + "</td>";
           html += "<td data-title='Cuisine'>" + d.getCuisineId().getName() + "</td>";
           html += "<td data-title='Cook'>" + d.getUserId().getUsername() + "</td>";
           html += "<td data-title='Cost'>" + d.getCost() + "</td>";
           html += "<td data-title='Reservation Time'>" + d.getReservationTime() + "</td>";
           html += "<td data-title='Meal Time'>" + d.getMealTime() + "</td>";
           html += "<td class='select'><a class='button' href='#'>Select</a></td>";
           html += "</tr>";
        }
        html += "</tbody></table>";
        
        return html;
    }

}
