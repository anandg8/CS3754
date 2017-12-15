/*
 * Created by Amit Dayal on 2017.12.12  * 
 * Copyright © 2017 Amit Dayal. All rights reserved. * 
 */
package com.mycompany.FacadeBeans;

import com.mycompany.EntityBeans.Comments;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author amitd
 */
@Stateless
public class CommentsFacade extends AbstractFacade<Comments> {

    @PersistenceContext(unitName = "CookToSharePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommentsFacade() {
        super(Comments.class);
    }
    
    /**
     * Get all the comments from the database where the comment_table_id is the comments_conjunction_id
     * @param comments_conjunction_id of the dish relating to this forum board
     * @return a list of comments
     */
    public List<Comments> getCommentsRecursively(int comments_conjunction_id) {
        Query q = getEntityManager().createNativeQuery("SELECT * FROM comments WHERE comment_table_id = ?", Comments.class);
        q.setParameter(1, comments_conjunction_id);
        List<Comments> parentComments = q.getResultList();
        return parentComments;
    }

    /**
     * Get the score or 'points' for the comment.
     * This will return a list of 'actions' which are are labeled as such:
     *      -1 (negative score)
     *      +1 (positive score)
     * Adding all these scores across all users gives us the final score
     * @param id of the comments id
     * @return the score of a comment
     */
    public int getScore(int id) {
        List<Integer> actionList = getEntityManager().createNativeQuery("SELECT action FROM votes WHERE comments_id = ?").setParameter(1, id).getResultList();
        //if -1, downvote, if 1, upvote
        int score = 0;
        for (Integer i : actionList) {
            score += i;
        }
        return score;
    }

    /**
     * Check if the user voted for a comment. We check this by counting the number of rows in the votes table
     * cross-referenced with the user_id and comments_id. If the number is equal to 1, we know that the user
     * has already voted for this comment
     * @param userid to check for
     * @param commentID to check again
     * @return true if voted for, false otherwise
     */
    public boolean didUserVote(Integer userid, int commentID) {
        Long i = (Long) getEntityManager().createNativeQuery("SELECT COUNT(*) FROM votes WHERE user_id = ? AND comments_id = ?").setParameter(1, userid).setParameter(2, commentID).getSingleResult();
        return i.intValue() == 1;
    }

    /**
     * Upvote a comment
     * @param userid that will upvote
     * @param commentid that the user will upvote
     * @param dishid of the associated comment
     */
    public void userUpvote(Integer userid, int commentid, int dishid) {
        getEntityManager().createNativeQuery("INSERT INTO votes VALUES(DEFAULT, ?, 1, ?, ?)")
                .setParameter(1, userid)
                .setParameter(2, commentid)
                .setParameter(3, dishid)
                .executeUpdate();
    }
    
    /**
     * Downvote a comment
     * @param userid that will upvote
     * @param commentid that the user will upvote
     * @param dishid of the associated comment
     */
    public void userDownvote(Integer userid, int commentid, int dishid) {
        getEntityManager().createNativeQuery("INSERT INTO votes VALUES(DEFAULT, ?, -1, ?, ?)")
                .setParameter(1, userid)
                .setParameter(2, commentid)
                .setParameter(3, dishid)
                .executeUpdate();
    }

    /**
     * Submit a comment
     * @param userid of the poster
     * @param commentsConjunctionID that this post relates to the selected dish
     * @param comment itself to store
     */
    public void submitComment(int userid, int commentsConjunctionID, String comment) {
        getEntityManager().createNativeQuery("INSERT INTO comments VALUES(DEFAULT, ?, 0, ?, DEFAULT, 1, ?)")
                .setParameter(1, userid)
                .setParameter(2, commentsConjunctionID)
                .setParameter(3, comment)
                .executeUpdate();
    }
    
}
