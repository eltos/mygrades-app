package de.mygrades.database.dao;

import java.util.List;
import de.mygrades.database.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "RULE".
 */
public class Rule {

    private Long id;
    private long universityId;
    /** Not-null value. */
    private String type;
    private java.util.Date lastUpdated;
    private long ruleId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RuleDao myDao;

    private List<Action> actions;
    private List<TransformerMapping> transformerMappings;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Rule() {
    }

    public Rule(Long id) {
        this.id = id;
    }

    public Rule(Long id, long universityId, String type, java.util.Date lastUpdated, long ruleId) {
        this.id = id;
        this.universityId = universityId;
        this.type = type;
        this.lastUpdated = lastUpdated;
        this.ruleId = ruleId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRuleDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(long universityId) {
        this.universityId = universityId;
    }

    /** Not-null value. */
    public String getType() {
        return type;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setType(String type) {
        this.type = type;
    }

    public java.util.Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(java.util.Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Action> getActions() {
        if (actions == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ActionDao targetDao = daoSession.getActionDao();
            List<Action> actionsNew = targetDao._queryRule_Actions(id);
            synchronized (this) {
                if(actions == null) {
                    actions = actionsNew;
                }
            }
        }
        return actions;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetActions() {
        actions = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<TransformerMapping> getTransformerMappings() {
        if (transformerMappings == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TransformerMappingDao targetDao = daoSession.getTransformerMappingDao();
            List<TransformerMapping> transformerMappingsNew = targetDao._queryRule_TransformerMappings(id);
            synchronized (this) {
                if(transformerMappings == null) {
                    transformerMappings = transformerMappingsNew;
                }
            }
        }
        return transformerMappings;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTransformerMappings() {
        transformerMappings = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Action> getActionsRaw() {
        return actions;
    }

    public void setTransformerMappings(List<TransformerMapping> transformerMappings) {
        this.transformerMappings = transformerMappings;
    }

    public List<TransformerMapping> getTransformerMappingsRaw() {
        return transformerMappings;
    }
    // KEEP METHODS END

}
