/*
 * Created by Osman Balci on 2017.11.21  * 
 * Copyright © 2017 Osman Balci. All rights reserved. * 
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
    
    public List<Comments> getCommentsRecursively(int comments_conjunction_id) {
        //System.out.println("comments conjunction id: " + comments_conjunction_id);
        Query q = getEntityManager().createNativeQuery("SELECT * FROM comments WHERE comment_table_id = ?", Comments.class);
        q.setParameter(1, comments_conjunction_id);
        List<Comments> parentComments = q.getResultList();
        return parentComments;
    }

    //id is the comment id
    public int getScore(int id) {
        List<Integer> actionList = getEntityManager().createNativeQuery("SELECT action FROM votes WHERE comments_id = ?").setParameter(1, id).getResultList();
        //if -1, downvote, if 1, upvote
        int score = 0;
        for (Integer i : actionList) {
            score += i;
        }
        return score;
    }

    public boolean didUserVote(Integer userid, int commentID) {
        Long i = (Long) getEntityManager().createNativeQuery("SELECT COUNT(*) FROM votes WHERE user_id = ? AND comments_id = ?").setParameter(1, userid).setParameter(2, commentID).getSingleResult();
        System.out.println("did user vote : " + i);
        System.out.println("userid : " + userid);
        System.out.println("comment id: " + commentID);
        return i.intValue() == 1;
    }

    public void userUpvote(Integer userid, int commentid, int dishid) {
        getEntityManager().createNativeQuery("INSERT INTO votes VALUES(DEFAULT, ?, 1, ?, ?)")
                .setParameter(1, userid)
                .setParameter(2, commentid)
                .setParameter(3, dishid)
                .executeUpdate();
    }
    
    public void userDownvote(Integer userid, int commentid, int dishid) {
        getEntityManager().createNativeQuery("INSERT INTO votes VALUES(DEFAULT, ?, -1, ?, ?)")
                .setParameter(1, userid)
                .setParameter(2, commentid)
                .setParameter(3, dishid)
                .executeUpdate();
    }

    public void submitComment(int userid, int commentsConjunctionID, String comment) {
        getEntityManager().createNativeQuery("INSERT INTO comments VALUES(DEFAULT, ?, 0, ?, DEFAULT, 1, ?)")
                .setParameter(1, userid)
                .setParameter(2, commentsConjunctionID)
                .setParameter(3, comment)
                .executeUpdate();
    }
    
}
