package com.frostwire.gui.library;

import java.io.File;

import javax.swing.JTable;

import com.limegroup.gnutella.FileDesc;
import com.limegroup.gnutella.gui.tables.ColoredCellImpl;
import com.limegroup.gnutella.gui.tables.HashBasedDataLineModel;
import com.limegroup.gnutella.gui.tables.SizeHolder;

/**
 * Library specific DataLineModel.
 * Uses HashBasedDataLineModel instead of BasicDataLineModel
 * for quicker access to row's based on the file.
 */
final class LibraryFilesTableModel extends HashBasedDataLineModel<LibraryFilesTableDataLine, File> {

	/**
     * 
     */
    private static final long serialVersionUID = 2859783399965055446L;
    
    /**
	 * The table this model is used for.
	 * (Needed to make sure isCellEditable
	 *  is only true when a single thing is selected.)
	 */
	//private JTable _table;

	LibraryFilesTableModel() {
	    super(LibraryFilesTableDataLine.class);
	}
	
    /**
     * Creates a new LibraryTableDataLine
     */
    public LibraryFilesTableDataLine createDataLine() {
        return new LibraryFilesTableDataLine(this);
    }    	

	/**
	 * Set the table this model is used for
	 * Needed for isCellEditable to work
	 */
	void setTable(JTable table) {
	    //_table = table;
	}

	/**
	 * Override the normal refresh.
	 * Because the DataLine's don't cache any data,
	 * we can just call update & they'll show the correct info
	 * now.
	 */
	public Object refresh() {
	    fireTableRowsUpdated(0, getRowCount());
	    return null;
	}

    /**
     * OVerride default so new ones get added to the end
     */
    @Override
    public int add(File o) {
        return add(o, getRowCount());
    }

	/**
	 * Override the dataline add so we can re-initialize files
	 * to include the FileDesc.  Necessary for changing pending status
	 * to shared status.
	 */
    @Override
	public int add(LibraryFilesTableDataLine dl, int row) {
	    File init = dl.getInitializeObject();
	    if ( !contains(init) ) {
	        return forceAdd(dl, row);
	    } else {
	        FileDesc fd = dl.getFileDesc();
	        if ( fd != null ) {
	            row = getRow(init);
	            get( row ).setFileDesc(fd);
	            fireTableRowsUpdated( row, row );
	        }
	        // we aren't going to use this dl, so clean it up.
	        dl.cleanup();
	    }
	    return -1;
    }
    
    /**
     * Reinitializes a dataline that is using the given initialize object.
     */
    void reinitialize(File f) {
        if(contains(f)) {
            int row = getRow(f);
            get(row).initialize(f);
            fireTableRowsUpdated(row, row);
        }
    }
    
    /**
     * Reinitializes a dataline from using one file to use another.
     */
    void reinitialize(File old, File now) {
        if(contains(old)) {
            int row = getRow(old);
            get(row).initialize(now);
            initializeObjectChanged(old, now);
            fireTableRowsUpdated(row, row);
        }
    }

	/**
	 * Returns the file extension for the given row.
	 *
	 * @param row  The row of the file
	 *
	 * @return  A <code>String</code> object containing the file extension
	 */
	String getType(int row) {
	    return (String)(
	             (ColoredCellImpl)get(row).getValueAt(
	                LibraryFilesTableDataLine.TYPE_IDX)).getValue();
	}

	/**
	 * Returns the file object stored in the given row.
	 *
	 * @param row  The row of the file
	 *
	 * @return  The <code>File</code> object stored at the specified row
	 */
	File getFile(int row) {
	    return get(row).getInitializeObject();
	}

	/**
	 * Returns the name of the file at the given row.
	 *
	 * @param row  The row of the file
	 *
	 * @return  A <code>String</code> object containing the name of the file
	 */
	String getName(int row) {
	    return (String)(
	        (ColoredCellImpl)get(row).getValueAt(
	            LibraryFilesTableDataLine.NAME_IDX)).getValue();
	}

 	/**
	 * Returns the name of the file at the given row.
	 *
	 * @param row  The row of the file
	 *
	 * @return  An <code>int</code> containing the size of the file
	 */
	long getSize(int row) {
	    return ((SizeHolder)(
	        (ColoredCellImpl)get(row).getValueAt(
                LibraryFilesTableDataLine.SIZE_IDX)).getValue()).getSize();
    }

	FileDesc getFileDesc(int row) {
	    return get(row).getFileDesc();
	}
	
	/**
	 * Returns a boolean specifying whether or not specific cell in the table
	 * is editable.
	 *
	 * @param row the row of the table to access
	 *
	 * @param col the column of the table to access
	 *
	 * @return <code>true</code> if the specified cell is editable,
	 *         <code>false</code> otherwise
	 */
	public boolean isCellEditable(int row, int col) {
//	    // check if renaming is allowed on selected table
//        if (!LibraryMediator.isRenameEnabled())
//            return false;
//		if (getFile(row).isDirectory())
//			return false;
//            
//		ListSelectionModel selModel = _table.getSelectionModel();
//		int min = selModel.getMinSelectionIndex();
//		int max = selModel.getMaxSelectionIndex();
//		
//		return min == max &&
//		       col == LibraryTableDataLine.NAME_IDX &&
//			   _table.getSelectedRow() == row;
	    return false;
	}
}