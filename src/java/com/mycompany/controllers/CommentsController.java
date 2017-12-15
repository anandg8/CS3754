package com.mycompany.controllers;

import com.mycompany.EntityBeans.Comments;
import com.mycompany.controllers.util.JsfUtil;
import com.mycompany.controllers.util.JsfUtil.PersistAction;
import com.mycompany.FacadeBeans.CommentsFacade;
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

@Named("commentsController")
@SessionScoped
public class CommentsController implements Serializable {

    /*
    The instance variable 'ejbFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the CommentsFacade object, after it is instantiated at runtime, into the instance variable 'ejbFacade'.
     */
    @EJB
    private com.mycompany.FacadeBeans.CommentsFacade ejbFacade;
    private List<Comments> items = null;
    private Comments selected;
    private String comment;
    
    @Inject
    private DishController dishController; // reference to the dishController.
    
    @Inject
    private AccountManager accountManager; // reference to the accountManager.

    public CommentsController() {
    }

    public Comments getSelected() {
        return selected; // return the selected comment.
    }

    public void setSelected(Comments selected) {
        this.selected = selected; // set the selected comment as the selected comment.
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CommentsFacade getFacade() {
        return ejbFacade; // return the facade instance variable.
    }
    
    public void setComment(String c){
        this.comment = c; // set the content of the comment.
    }
    
    public String getComment() {
        return this.comment; // return the comment.
    }
    
    
    public String submitReply() {
        System.out.println("Reply is: " + this.comment);
        // update the forum based on the reply.
        if (this.comment != null && this.comment.trim().length() > 0) {
            getFacade().submitComment(accountManager.getSelected().getId(), dishController.getCommentsConjunctionID(), this.comment);
            this.comment = null;
            // after updating, return to the forum board to see the new comment.
            return "RateForumBoard.xhtml?faces-redirect=true";
        } else {
            // user did not fill out the comment field.
            JsfUtil.addErrorMessage("Comment was left blank");   
        }
        return "";
    }
    
    public String upvote(int id) {
        // don't let the user upvote twice.
        if (getFacade().didUserVote(accountManager.getSelected().getId(), id)) {
            JsfUtil.addErrorMessage("You already voted for this comment!");
        } else {
            // return to the forum board to see how the user voted.
            getFacade().userUpvote(accountManager.getSelected().getId(), id, dishController.getSelected().getId());
            return "RateForumBoard.xhtml?faces-redirect=true";
        }
        return "";
    }
    
    public String downvote(int id) {
        // don't let the user downvote twice.
        if (getFacade().didUserVote(accountManager.getSelected().getId(), id)) {
            JsfUtil.addErrorMessage("You already voted for this comment!");
        } else {
            // return to the forum board to see how the user voted.
            getFacade().userUpvote(accountManager.getSelected().getId(), id, dishController.getSelected().getId());
            return "RateForumBoard.xhtml?faces-redirect=true";
        }
        return "";
    }
    
    public List<Comments> getCommentsList() {
        // return the list of all the comments on the forum.
        items = getFacade().getCommentsRecursively(dishController.getCommentsConjunctionID()); //get a list of all the comments for a certain dish
        return items;
    }
    
    public int getScore(int id) {
        // get the vote count of a comment on the forum.
        return getFacade().getScore(id);
    }

    public Comments prepareCreate() {
        selected = new Comments();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CommentsCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CommentsUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CommentsDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Comments> getItems() {
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
                    getFacade().edit(selected);
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

    public Comments getComments(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Comments> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Comments> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Comments.class)
    public static class CommentsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CommentsController controller = (CommentsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "commentsController");
            return controller.getComments(getKey(value));
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
            if (object instanceof Comments) {
                Comments o = (Comments) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Comments.class.getName()});
                return null;
            }
        }

    }

}
