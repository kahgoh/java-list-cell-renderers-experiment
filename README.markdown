Java List Cell Renderers
========================
I created this project to experiment with [ListCellRenderers](http://download.oracle.com/javase/7/docs/api/javax/swing/ListCellRenderer.html) with a changing look and feel. It consists of an application (defined in `CellRenderer`) that brings up a window containing several [JComboBoxes](http://download.oracle.com/javase/7/docs/api/javax/swing/JComboBox.html) with different implementations of [ListCellRenderers](http://download.oracle.com/javase/7/docs/api/javax/swing/ListCellRenderer.html) for comparing the effect of the renderes on the combo box. 

Note that the text field at the bottom of the dialog does nothing. It is there so that focus (and, thus, the focus highlighting) can be taken off from the combo boxes.

Project Notes
-------------
The code is checked in as an [Eclipse](http://www.eclipse.org) project and is configured with the [Maven][Maven] nature (using the [m2e](http://www.eclipse.org/m2e/) plugin). The project requires the following:

* [MigLayout](http://www.miglayout.com)
* [Substance](http://substance.java.net) (there is also a [GitHub repository](https://github.com/kirillcool/substance) for it)

If wish to run the program, the main class is `CellRenderers`.

License
-------
Released under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).

[Maven]: http://maven.apache.org
