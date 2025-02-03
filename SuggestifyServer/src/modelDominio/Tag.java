package modelDominio;

import java.io.Serializable;

public class Tag implements Serializable{
    private static final long serialVersionUID = 123456789;
    private int idTag;
    private String tagTexto;

    public Tag(int idTag, String tagTexto) {
        this.idTag = idTag;
        this.tagTexto = tagTexto;
    }

    public Tag(int idTag) {
        this.idTag = idTag;
    }

    public Tag(String tagTexto) {
        this.tagTexto = tagTexto;
    }

    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int idTag) {
        this.idTag = idTag;
    }

    public String getTagTexto() {
        return tagTexto;
    }

    public void setTagTexto(String tagTexto) {
        this.tagTexto = tagTexto;
    }
    
    
    
}
