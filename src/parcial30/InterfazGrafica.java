/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial30;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


class InterfazGrafica {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Persona> listaPersonas;

    public InterfazGrafica() {
        listaPersonas = new ArrayList<>();
        frame = new JFrame("Gestor de Archivos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"Identificación", "Nombre", "Correo"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 2));

        JButton btnGuardarPlano = new JButton("Guardar Archivo Plano");
        JButton btnLeerPlano = new JButton("Leer Archivo Plano");
        JButton btnGuardarXML = new JButton("Guardar Archivo XML");
        JButton btnLeerXML = new JButton("Leer Archivo XML");
        JButton btnGuardarJSON = new JButton("Guardar Archivo JSON");
        JButton btnLeerJSON = new JButton("Leer Archivo JSON");

        panelBotones.add(btnGuardarPlano);
        panelBotones.add(btnLeerPlano);
        panelBotones.add(btnGuardarXML);
        panelBotones.add(btnLeerXML);
        panelBotones.add(btnGuardarJSON);
        panelBotones.add(btnLeerJSON);

        frame.add(panelBotones, BorderLayout.SOUTH);

        // Formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(4, 2));
        JTextField txtIdentificacion = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtCorreo = new JTextField();
        JButton btnAgregar = new JButton("Agregar Persona");

        panelFormulario.add(new JLabel("Identificación:"));
        panelFormulario.add(txtIdentificacion);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Correo:"));
        panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel());
        panelFormulario.add(btnAgregar);

        frame.add(panelFormulario, BorderLayout.NORTH);

        btnAgregar.addActionListener(e -> {
            try {
                String identificacion = txtIdentificacion.getText();
                String nombre = txtNombre.getText();
                String correo = txtCorreo.getText();
                Persona persona = new Persona (identificacion, nombre, correo);
                listaPersonas.add(persona);
                tableModel.addRow(new Object[]{identificacion, nombre, correo});
                txtIdentificacion.setText("");
                txtNombre.setText("");
                txtCorreo.setText("");
            } catch (ExcepcionPersonalizada ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnGuardarPlano.addActionListener(e -> guardarArchivoPlano());
        btnLeerPlano.addActionListener(e -> leerArchivoPlano());

        btnGuardarXML.addActionListener(e -> guardarArchivoXML());
        btnLeerXML.addActionListener(e -> leerArchivoXML());

        btnGuardarJSON.addActionListener(e -> guardarArchivoJSON());
        btnLeerJSON.addActionListener(e -> leerArchivoJSON());

        frame.setVisible(true);
    }

    // Métodos para manejo de archivo plano
    private void guardarArchivoPlano() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("personas.txt"))) {
            for (Persona persona : listaPersonas) {
                writer.write("," + persona.getIdentificacion() + persona.getNombre() + "," + persona.getCorreo());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(frame, "Archivo plano guardado correctamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error al guardar el archivo plano.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void leerArchivoPlano() {
        try (BufferedReader reader = new BufferedReader(new FileReader("personas.txt"))) {
            listaPersonas.clear();
            tableModel.setRowCount(0);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                listaPersonas.add(new Persona(data[0], data[1], data[2]));
                tableModel.addRow(data);
            }
            JOptionPane.showMessageDialog(frame, "Archivo plano leído correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error al leer el archivo plano.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarArchivoXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement = doc.createElement("personas");
            doc.appendChild(rootElement);

            for (Persona persona : listaPersonas) {
                Element personaElement = doc.createElement("persona");

                Element idElement = doc.createElement("identificacion");
                idElement.appendChild(doc.createTextNode(persona.getIdentificacion()));
                personaElement.appendChild(idElement);

                Element nombreElement = doc.createElement("nombre");
                nombreElement.appendChild(doc.createTextNode(persona.getNombre()));
                personaElement.appendChild(nombreElement);

                Element correoElement = doc.createElement("correo");
                correoElement.appendChild(doc.createTextNode(persona.getCorreo()));
                personaElement.appendChild(correoElement);

                rootElement.appendChild(personaElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("personas.xml"));
            transformer.transform(source, result);

            JOptionPane.showMessageDialog(frame, "Archivo XML guardado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error al guardar el archivo XML.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void leerArchivoXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("personas.xml"));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("persona");
            listaPersonas.clear();
            tableModel.setRowCount(0);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String identificacion = element.getElementsByTagName("identificacion").item(0).getTextContent();
                    String nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
                    String correo = element.getElementsByTagName("correo").item(0).getTextContent();
                    listaPersonas.add(new Persona(identificacion, nombre, correo));
                    tableModel.addRow(new Object[]{identificacion, nombre, correo});
                }
            }
            JOptionPane.showMessageDialog(frame, "Archivo XML leído correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error al leer el archivo XML.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void guardarArchivoJSON() {
        try (Writer writer = new FileWriter("personas.json")) {
            Gson gson = new Gson();
            gson.toJson(listaPersonas, writer);
            JOptionPane.showMessageDialog(frame, "Archivo JSON guardado correctamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error al guardar el archivo JSON.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void leerArchivoJSON() {
        try (Reader reader = new FileReader("personas.json")) {
            Gson gson = new Gson();
            listaPersonas = gson.fromJson(reader, new TypeToken<List<Persona>>() {}.getType());
            tableModel.setRowCount(0);
            for (Persona persona : listaPersonas) {
                tableModel.addRow(new Object[]{persona.getIdentificacion(), persona.getNombre(), persona.getCorreo()});
            }
            JOptionPane.showMessageDialog(frame, "Archivo JSON leído correctamente.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error al leer el archivo JSON.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

