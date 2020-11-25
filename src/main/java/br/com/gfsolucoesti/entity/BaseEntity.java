package br.com.gfsolucoesti.entity;

import java.io.Serializable;

import br.com.gfsolucoesti.utils.GFUtils;

public abstract class BaseEntity implements Serializable{

    private static final long serialVersionUID = 5846807009125370361L;

    public abstract long getId();
    public abstract void setId(long id);

    public String getIdHash() {
        return GFUtils.encodeBase64(String.valueOf(getId()));
    }

    public void setIdHash(String hash) {
        try {
            setId(Long.parseLong(GFUtils.decodeBase64(hash)));
        } catch (Exception e) {
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        BaseEntity ent = (BaseEntity) obj;
        return ent.getId() == this.getId();
    }

}
