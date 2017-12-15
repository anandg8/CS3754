/*
 * Created by Amit Dayal on 2017.12.12  * 
 * Copyright © 2017 Amit Dayal. All rights reserved. * 
 */
package com.mycompany.EntityBeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amitd
 */
@Entity
@Table(name = "comments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comments.findAll", query = "SELECT c FROM Comments c")
    , @NamedQuery(name = "Comments.findById", query = "SELECT c FROM Comments c WHERE c.id = :id")
    , @NamedQuery(name = "Comments.findByReplyId", query = "SELECT c FROM Comments c WHERE c.replyId = :replyId")
    , @NamedQuery(name = "Comments.findByDatePosted", query = "SELECT c FROM Comments c WHERE c.datePosted = :datePosted")
    , @NamedQuery(name = "Comments.findByIsVisible", query = "SELECT c FROM Comments c WHERE c.isVisible = :isVisible")
    , @NamedQuery(name = "Comments.findByComment", query = "SELECT c FROM Comments c WHERE c.comment = :comment")})
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reply_id")
    private int replyId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_posted")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePosted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_visible")
    private int isVisible;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "comment")
    private String comment;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;
    @JoinColumn(name = "comment_table_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CommentsConjunction commentTableId;
    
    /* CONSTRUCTORS */
    public Comments() {
    }

    public Comments(Integer id) {
        this.id = id;
    }

    
    public Comments(Integer id, int replyId, Date datePosted, int isVisible, String comment) {
        this.id = id;
        this.replyId = replyId;
        this.datePosted = datePosted;
        this.isVisible = isVisible;
        this.comment = comment;
    }

    /**
     * Get the id of the comment
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the id of the comment
     * @param id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the reply id
     * @return reply id
     */
    public int getReplyId() {
        return replyId;
    }

    /**
     * Set the reply id
     * @param replyId to set to
     */
    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    /**
     * Get the date posted
     * @return string-formatted date of the date posted
     */
    public String getDatePosted() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/YY");
        String s = df.format(datePosted);
        return s;
    }

    /**
     * Set the date posted
     * @param datePosted set the date posted
     */
    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    /**
     * Get if the comment is visible
     * @return 1 true, 0 false
     */
    public int getIsVisible() {
        return isVisible;
    }

    /**
     * Set the visibility. This essentially 'deletes' a comment
     * @param isVisible 
     */
    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Get the comment itself
     * @return a string for the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the comment
     * @param comment set the comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Get the user who posted this comment
     * @return User object
     */
    public User getUserId() {
        return userId;
    }

    /**
     * Set the user of this comment
     * @param userId of this comment
     */
    public void setUserId(User userId) {
        this.userId = userId;
    }

    /**
     * Get the dish associated with this comment using the getcommentTable id
     * This is a conjunction table
     * @return CommentsConjunction object
     */
    public CommentsConjunction getCommentTableId() {
        return commentTableId;
    }

    /**
     * Set the comment Table id
     * @param commentTableId 
     */
    public void setCommentTableId(CommentsConjunction commentTableId) {
        this.commentTableId = commentTableId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comments)) {
            return false;
        }
        Comments other = (Comments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.EntityBeans.Comments[ id=" + id + " ]";
    }
    
}
