   M S B A M L   `   `   `  ����E  ABiblioteca, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null'clr-namespace:Biblioteca
Biblioteca  5   	       Biblioteca.Consultar   5   	   W SPresentationCore, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35e aSystem.Windows.Controls.Ribbon, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089R NWindowsBase, Version=6.0.2.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     5   	   R NSystem.Xaml, Version=6.0.2.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   *localclr-namespace:Biblioteca     5   	     � WindowStartupLocation$  CenterScreen=�5   	    � 
ResizeMode$ CanMinimize=�5	   	   $	��450��5
   	   $	��450��6   $	��800��6)   $	��800��68    � Title$ 📖 Consultar��5   	   $	��450��6   $	��800��6+    � 	Resources 5       �� 
TargetType �� Property �� Value=  7System.Windows.Controls.Primitives.DataGridColumnHeader:  4System.Windows.Controls.Primitives.DatePickerTextBox �� Path �� RelativeSource	 �� AncestorType(  "System.Windows.Controls.DatePicker
 �� StringFormat��%}  '��       '�� n     '�� ]    '  �    '  �    �� 5   
   " ��6   .�5      ��  � ��$ 12(�6)   �� 5       � ��$
 Bold"�6+   5      �� 5   
   " ��6   .�5      ��  � ��$ 12(�6)   �� 5       � ��$ Center=�62   �� 5       � ��$ 19��6'   �� 5       � �� �#E8EAF66+   �� 5       �@��
IsReadOnly$
 True��6+   5      �� 5   
   " ��6   .�5      ��  � ��$
 Bold"�6+   �� 5       � �� �#7986CB6+   �� 5       � ��
 �����6+   5      �� 5   
   "  6   .�5      ��  � ��$
 Bold"�6+   �� 5       � �� �#9FA8DA6+   �� 5        � ��$ Center=�6;   5!      �� 5"   
   "  6   .�5#      ��  � �� 5$      �� 5%      .R�5&      �� ��6"   �� $ SelectedDate� �� "	  $
 
dd/MM/yyyy��5'      5)      5*      5+      .��5,      � ���#C5CAE9.+�5-   
   �� $	��170��6   $	��Top=�6"   .+�5.      �� -   5         ��Name$ gpbDatosLibro��5.      $��DATOS LIBRO��6.   $	��150��6C   $��10q�6P   .��5/      � .+�50      �� $��Título:��6   �� 51      -   5         ��Name$ 	txtTitulo��51       �� TextWrapping$
 Wrap=�61   $�� ��6E   $	��300��6M   $��60,0,0,0q�6Y   �� 52      $��Autor:��6   $��20,0,0,0q�6-   �� 53      -   5        $ txtAutor��53      $
 Wrap=�60   $�� ��6D   $	��260��6L   $��10,0,0,0q�6X   �� 54      $��
Editorial:��6   �� 55      -   5        $ txtEditorial��55      $
 Wrap=�64   $�� ��6H   $	��150��6P   $��45,0,0,0q�6\   �� 56      $��Fecha publicación:��6   $��20,0,0,0q�69     57      -   5          Name$ dpFechaPublicacion��57   !   $	��120��6=   $��10,0,0,0q�6I   ${�False��6[   �� 58      $��Imagen:��6   $��20,0,0,0q�6.   �� 59      -   5        $ 	txtImagen��59      $
 Wrap=�61   $�� ��6E   $	��137��6M   $��10,0,0,0q�6Y   �� 5:      $��Descripción:��6   �� 5;      -   5        $ txtDescripcion��5;      $
 Wrap=�66   $�� ��6J   $	��636��6R   $��45��6^   $��27,3,0,3q�6j   $
��Auto=�6|   �� 5<      $��Precio:��6   �� 5=      -   5        $ 	txtPrecio��5=      $
 Wrap=�61   $�� ��6E   $	��120��6M   $��57,0,0,0q�6Y   �� 5>      $��Unidades Almacen:��6   $��20,0,0,0q�68   �� 5?      -	   5        $ txtUnidadesAlmacen��5?      $
 Wrap=�6:   $�� ��6N   $	��120��6V   $��10,0,0,0q�6b   �� 5@      -
   5         ��Name$ chkVenta��5@      $��¿En Venta?��61   $��Center=�6F   $��40,0,0,0q�6a   $
��Bold"�6s   ���#E8EAF6${�False��6�   5A      5B      5C      �� 5E   
   $��ELIGA UN
METODO:��6   $
��Left=�60   $��Center=�6K   $��	20,0,0,50q�6f   $��14(�6y   	� 5F   
   -   5         	�Name$ rdbId��5F      $	��ID:��6]   $
��Left=�6k   $��Center=�6�   $��
110,0,0,65q�6�   $��16(�6�   ���#E8EAF6$
��Bold"�6�   $��Center=�6�    d� 	IsChecked 6%   ��  �� ElementName$ txtId��$ 	IsEnabled�6�   �� 5G   
   -   5        $ txtId��5G      $
��Left=�6!   $��Center=�6<   $��
210,0,0,65q�6W   $��50��6k   $��Center=�6v   $��Center=�6�    �� 
IsReadOnly$ False��6�   	� 5H   
   -   5        $ 	rdbTitulo��5H      $��TITULO:��6m   $
��Left=�6   $��Center=�6�   $��
110,0,0,25q�6�   $��16(�6�   ���#E8EAF6$
��Bold"�6�   $��Center=�6�    6)   �� $ txtConsultaTitulo��$ 	IsEnabled�6�   �� 5I   
   -   5        $ txtConsultaTitulo��5I      $
��Left=�6-   $��Center=�6H   $��
210,0,0,25q�6c   $	��150��6w   $��Center=�6�   $��Center=�6�   $ False��6�   �� 5J   
   -   5         ��Name$ btnConsultar��5J      $��	CONSULTAR��6'   $��Right=�6;   $��Center=�6W   $��
0,0,285,40q�6r   $	��103��6�   $��29��6�   6�   �� 5K   
   -   5        $ 
btnLimpiar��5K      $��LIMPIAR��6%   $��Right=�67   $��Center=�6S   $��
0,0,160,40q�6n   $	��103��6�   $��29��6�   6�   �� 5L   
   -   5        $ btnSalir��5L      $��SALIR��6#   $��Right=�63   $��Center=�6O   $��	0,0,40,40q�6j   $	��103��6}   $��29��6�   6�   �� 5N   
   $	��220��6   $��Bottom=�6"   .+�5O      �� $��CATÁLOGO DE LIBROS��6   $	��200��63   $��10q�6@   .��5P      &   System.Windows.Controls.DataGrid  -   5          Name$ 
dgLibrosId��5P         AutoGenerateColumns$ False��6/      
IsReadOnly$
 True��6K      Columns 5Q      0  *System.Windows.Controls.DataGridTextColumn  5R      ,  &System.Windows.Controls.DataGridColumn   Header$ Id��6I      Width5  /System.Windows.Controls.DataGridLengthConverter$	 106 6U   1  +System.Windows.Controls.DataGridBoundColumn   Binding 6-   �� $ Id�6U     5S      $ Titulo��6M   $	 207 6]    6-   �� $ Titulo�6]     5T      $ Autor��6L   $	 207 6[    6-   �� $ Autor�6[     5U      $ 	Editorial��6P   $	 207 6c    6-   �� $ 	Editorial�6c   5W      5X      5Y      5Z      5[      