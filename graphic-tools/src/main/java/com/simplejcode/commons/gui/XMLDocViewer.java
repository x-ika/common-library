package com.simplejcode.commons.gui;

import org.w3c.dom.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Comparator;

public class XMLDocViewer extends JPanel implements Comparator<String> {

    public XMLDocViewer(Document doc) {
        NodeList list = doc.getDocumentElement().getElementsByTagName("row");

        String[] colNames = new String[list.item(0).getChildNodes().getLength() / 2 + 1];
        String[][] rows = new String[list.getLength()][colNames.length];

        colNames[0] = "#";
        for (int i = 0; i < rows.length; i++) {
            rows[i][0] = "" + i;
            NodeList nodeList = list.item(i).getChildNodes();
            for (int j = 1; j < colNames.length; j++) {
                Node node = nodeList.item(2 * j - 1);
                colNames[j] = node.getNodeName();
                rows[i][j] = node.getChildNodes().item(0).getNodeValue();
            }
        }

        JTable table = new JTable(rows, colNames);
        table.setRowHeight(20);
        table.setRowMargin(5);
        table.getColumnModel().setColumnMargin(5);
        table.setAutoCreateRowSorter(true);

        TableRowSorter<TableModel> sorter =
                new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        M:
        for (int j = 0; j < colNames.length; j++) {
            for (String[] row : rows) {
                try {
                    Double.valueOf(row[j]);
                } catch (Exception e) {
                    continue M;
                }
            }
            sorter.setComparator(j, this);
        }


        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(900, 600));
        add(scrollPane);
    }

    public int compare(String o1, String o2) {
        return Double.compare(Double.parseDouble(o1), Double.parseDouble(o2));
    }
}
