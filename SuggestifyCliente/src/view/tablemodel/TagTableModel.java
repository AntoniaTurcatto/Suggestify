package view.tablemodel;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import modelDominio.Tag;

public class TagTableModel extends AbstractTableModel{
    
    private ArrayList<Tag> listaTags;

    public TagTableModel(ArrayList<Tag> listaTags) {
        this.listaTags = listaTags;
    }

    @Override
    public int getRowCount() {
        return listaTags.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Tag tg = listaTags.get(rowIndex);
        switch (columnIndex) {
            case 0: return tg.getIdTag();
            case 1: return tg.getTagTexto();
            default: return "[ERRO]";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID";
            case 1: return "TAG";
            default: return "[ERRO]";
        }
    }
    
    public Tag getTag(int row){
        return listaTags.get(row);
    }
    
}
