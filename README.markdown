Java List Cell Renderers
========================
I created this project to experiment with [ListCellRenderers](http://download.oracle.com/javase/7/docs/api/javax/swing/ListCellRenderer.html)] with a changing look and feel. It consists of an application (defined in {{CellRenderer}}) that brings up a window containing several [JComboBoxes](http://download.oracle.com/javase/7/docs/api/javax/swing/JComboBox.html) with different implementations of [ListCellRenderers](http://download.oracle.com/javase/7/docs/api/javax/swing/ListCellRenderer.html)] for comparing the effect of the renderes on the combo box.

Running the Code
----------------
The code is checked in as an [Eclipse](http://www.eclipse.org) project and configured with the [Maven][Maven] nature (using the [m2e](http://www.eclipse.org/m2e/) plugin). The main class is called `CellRenderer`. The project also depends on (these are listed in the `pom.xml` for [Maven][Maven]):
* [MigLayout](http://www.miglayout.com)
* [Substance](http://substance.java.net) (there is also a [GitHub repository](https://github.com/kirillcool/substance) for it)

License
-------
Released under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).

[Maven]: http://maven.apache.org
