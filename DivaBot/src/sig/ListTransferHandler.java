package sig;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class ListTransferHandler extends TransferHandler {
    private int[] indices = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.
	public static DefaultListModel start;
            
    /**
     * We only support importing strings.
     */
    public boolean canImport(TransferHandler.TransferSupport info) {
        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }
        return true;
   }

    /**
     * Bundle up the selected items in a single list for export.
     * Each line is separated by a newline.
     */
    protected Transferable createTransferable(JComponent c) {
        JList list = (JList)c;
        start = (DefaultListModel)list.getModel();
        indices = list.getSelectedIndices();
        //System.out.println("Selected indexes: "+Arrays.toString(indices));
        
        Object[] values = list.getSelectedValues();
        
        StringBuffer buff = new StringBuffer();

        for (int i = 0; i < values.length; i++) {
            Object val = values[i];
            buff.append(val == null ? "" : val.toString());
            if (i != values.length - 1) {
                buff.append("\n");
            }
        }
       // System.out.println(buff.toString());
        
        return new StringSelection(buff.toString());
    }
    
    /**
     * We support both copy and move actions.
     */
    public int getSourceActions(JComponent c) {
        return TransferHandler.MOVE;
    }
    
    /**
     * Perform the actual import.  This demo only supports drag and drop.
     */
    @SuppressWarnings("unchecked")
	public boolean importData(TransferHandler.TransferSupport info) {
        if (!info.isDrop()) {
            return false;
        }

        JList list = (JList)info.getComponent();
        DefaultListModel listModel = (DefaultListModel)list.getModel();
        JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
        int index = dl.getIndex();
        boolean insert = dl.isInsert();

        // Get the string that is being dropped.
        Transferable t = info.getTransferable();
        String data;
        try {
            data = (String)t.getTransferData(DataFlavor.stringFlavor);
        } 
        catch (Exception e) { return false; }
                                
        // Wherever there is a newline in the incoming data,
        // break it into a separate item in the list.
        String[] values = data.split("\n");
        
        addIndex = index;
        addCount = values.length;
        
        //System.out.println(listModel);
        for (int i=0;i<values.length;i++) {
        	listModel.add(listModel.getSize(),values[i]);
        }
        if (!start.equals(listModel)) {
	        for (int i=0;i<values.length;i++) {
	        	for (int j=0;j<start.getSize();j++) {
	        		if (values[i].equals(start.get(j))) {
	        			start.remove(j);
	        			break;
	        		}
	        	}
	        }
        }
        //System.out.println(start);
        //System.out.println(listModel);
        if (listModel.equals(DisplayManager.model2)) {
	        String[] labels = new String[listModel.getSize()];
	        for (int i=0;i<listModel.getSize();i++) {
	        	labels[i]=(String)listModel.get(i);
	        }
	        DisplayManager.selectedDisplay.labels=labels;
        } else {
	        String[] labels = new String[start.getSize()];
	        for (int i=0;i<start.getSize();i++) {
	        	labels[i]=(String)start.get(i);
	        }
	        DisplayManager.selectedDisplay.labels=labels;
        }
        DisplayManager.selectedDisplay.nextUpdateTime=0;
        DisplayManager.selectedDisplay.forceUpdate=true;
        MyRobot.p.repaint();
        //System.out.println("Selected indexes: "+Arrays.toString(indices));
        for (int i=0;i<indices.length;i++) {
        	if (addIndex<indices[i]) {
	        	listModel.add(addIndex, listModel.get(indices[i]));
                //list.setSelectedValue(listModel.get(indices[i]), true);
	        	listModel.remove(indices[i]+1);
        	} else {
                //list.setSelectedIndex(addIndex-1);
        		listModel.add(addIndex, listModel.get(indices[i]));
                //list.setSelectedValue(listModel.get(indices[i]), true);
	        	listModel.remove(indices[i]);
        	}
        }
        //System.out.println(listModel);
        return true;
    }

    /**
     * Remove the items moved from the list.
     */
    protected void exportDone(JComponent c, Transferable data, int action) {
        JList source = (JList)c;
        DefaultListModel listModel  = (DefaultListModel)(source.getModel());

        if (action == TransferHandler.MOVE) {
        	int amountRemoved=0;
            for (int i = 0; i < indices.length; i++) {
                //listModel.remove(indices[i]-(amountRemoved++));
            }
        }
        
        indices = null;
        addCount = 0;
        addIndex = -1;
    }
}