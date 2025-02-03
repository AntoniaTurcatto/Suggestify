package view.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import modelDominio.Artista;

public class RendererListArtistas extends JLabel implements ListCellRenderer<Artista> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Artista> list, Artista value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.getNomeArtistico());
        setOpaque(true);
        setBackground(isSelected ? new Color(0x5755DF) : new Color(80,80,80));
        setForeground(Color.white);
        return this;
    }

}
