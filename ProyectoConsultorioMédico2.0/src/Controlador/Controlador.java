/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 */
package Controlador;

import Vista.InterfazGrafica;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import Conexion.Pacientes;
import Conexion.PacientesDAO;

public class Controlador {
    PacientesDAO dao = new PacientesDAO();
    Pacientes p = new Pacientes();
    InterfazGrafica v;
    DefaultTableModel modelo = new DefaultTableModel();

    public Controlador(InterfazGrafica vista) {
        this.v = vista;
        cargarEspecialidades();

        v.btnAgregar.addActionListener(e -> agregar());
        v.btnEliminar.addActionListener(e -> eliminar());
        v.btnLimpiar.addActionListener(e -> limpiar());
        v.btnListar.addActionListener(e -> listar(v.tabla));
    }

    private void cargarEspecialidades() {
        v.jcbEspecialidad.removeAllItems();
        List<String> especialidades = dao.listarEspecialidades();
        for (String esp : especialidades) {
            v.jcbEspecialidad.addItem(esp);
        }
    }

    public void listar(JTable tabla) {
        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
            "Documento", "Nombre", "Afiliación", "Edad", "Especialidad","Fecha", "Total a Pagar"});
        modelo.setRowCount(0);

        List<Pacientes> lista = dao.listar();
        for (int i = 0; i < lista.size(); i++) {
            Object[] objeto = new Object[7];
            objeto[0] = lista.get(i).getDocumentoPaciente();
            objeto[1] = lista.get(i).getNombre();
            objeto[2] = lista.get(i).getTipoAfiliacion();
            objeto[3] = lista.get(i).getEdad();
            objeto[4] = lista.get(i).getEspecialidad();
            objeto[5] = lista.get(i).getFecha();
            objeto[6] = String.format("$%.0f", lista.get(i).getTotalPagar());
            modelo.addRow(objeto);
        }
        tabla.setModel(modelo);

        v.txtTotalPacientes.setText(String.valueOf(lista.size()));
    }

    private void agregar() {
        if (v.txtNombre.getText().trim().isEmpty() ||
            v.txtDocumento.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(v, "Nombre y Documento son obligatorios");
            return;
        }

        String afiliacion = "";
        if (v.rbtContributivo.isSelected()) {
            afiliacion = "Contributivo";
        } else if (v.rbtSubsidiado.isSelected()) {
            afiliacion = "Subsidiado";
        } else {
            javax.swing.JOptionPane.showMessageDialog(v, "Seleccione tipo de afiliación");
            return;
        }

        int idEspecialidad = v.jcbEspecialidad.getSelectedIndex() + 1;

        p.setDocumentoPaciente(v.txtDocumento.getText().trim());
        p.setNombre(v.txtNombre.getText().trim());
        p.setTipoAfiliacion(afiliacion);
        p.setEdad(Integer.parseInt(v.txtEdad.getText().trim()));

        int r = dao.insertar(p, idEspecialidad);
        if (r == 1) {
            javax.swing.JOptionPane.showMessageDialog(v, "Paciente agregado con éxito");
            listar(v.tabla);
            limpiar();
        } else {
            javax.swing.JOptionPane.showMessageDialog(v, "No se pudo agregar");
        }
    }

    private void eliminar() {
        int fila = v.tabla.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(v,
                "Debe seleccionar un paciente de la tabla");
            return;
        }
        String documento = modelo.getValueAt(fila, 0).toString();
        int confirm = javax.swing.JOptionPane.showConfirmDialog(v,
            "¿Desea eliminar el paciente: " + documento + "?",
            "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            int r = dao.eliminar(documento); 
            if (r == 1) {
                javax.swing.JOptionPane.showMessageDialog(v, "Paciente eliminado");
                listar(v.tabla); 
            } else {
                javax.swing.JOptionPane.showMessageDialog(v, "No se pudo eliminar");
            }
        }
    }

    private void limpiar() {
        v.txtNombre.setText("");
        v.txtDocumento.setText("");
        v.txtEdad.setText("");
        v.rbtContributivo.setSelected(false);
        v.rbtSubsidiado.setSelected(false);
        v.txtNombre.requestFocus();
    }
}