package view.util;

import java.util.ArrayList;
import javax.swing.AbstractListModel;
import modelDominio.Artista;

public class ListArtistas extends AbstractListModel<Artista>{ 
    private ArrayList<Artista> listaArtista;
    
    public ListArtistas(ArrayList<Artista> listaArtista){
        this.listaArtista = listaArtista;
    }

    @Override
    public int getSize() {
        return listaArtista.size();
    }

    @Override
    public Artista getElementAt(int index) {
        return listaArtista.get(index);
    }
    
    public ArrayList<Artista> getElements(){
        return listaArtista;
    }
    
}
