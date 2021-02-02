package Activitys;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import Fun.ConvertDate;
import Fun.HintTextField;
import Fun.dataConnection;
import OOP.hoso;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChinhHoSo extends JFrame {

	private JPanel contentPane;
	private JTextField textngayvay;
	private JTextField textsotien;
	Vector<String> ten;
	Vector<String> ma;
	Vector<String> tensv;
	Vector<String> masvl;
	Sua shs;
	int check;
	public ChinhHoSo(String dngayvay, String dsotien, String dmasv,String dten,String dnganhang,Sua suaHoSo, int n) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(880, 180, 320, 325);
		contentPane = new JPanel();
		check =n;
		contentPane.setBackground(new Color(64, 157, 250));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(5,2));
		shs=suaHoSo;
		JLabel lblhoten = new JLabel("Họ tên");
		lblhoten.setForeground(new Color(255, 255, 255));
		lblhoten.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lblhoten);
		JComboBox comboxTen = new JComboBox();
		contentPane.add(comboxTen);
		JLabel lblngayvay = new JLabel("Ngày vay");
		lblngayvay.setForeground(new Color(255, 255, 255));
		lblngayvay.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lblngayvay);
		textngayvay = new HintTextField("yyyy-mm-dd");	
		contentPane.add(textngayvay);
		JLabel lblsotien = new JLabel("Số tiền");
		lblsotien.setForeground(new Color(255, 255, 255));
		lblsotien.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lblsotien);
		textngayvay.setColumns(10);
		textsotien = new JTextField();
		contentPane.add(textsotien);
		textsotien.setColumns(10);
		JLabel lblnganhang = new JLabel("Ngân hàng");
		lblnganhang.setForeground(new Color(255, 255, 255));
		lblnganhang.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lblnganhang);
		JComboBox comboBox = new JComboBox();
		contentPane.add(comboBox);
		JButton btnhuy = new JButton("Hủy");
		btnhuy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		Connection conn = new dataConnection().ConnectDB();
		if (check==1) {
			ten = new Vector<String>();
			ma = new Vector<String>();
			tensv = new Vector<String>();
			masvl = new Vector<String>();
			
			try {
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM nganhang");
				while (rs.next()) {
					ten.add(rs.getString(2));
					ma.add(rs.getString(1));
				}
				rs = statement.executeQuery("SELECT * FROM sinhvien");
				while (rs.next()) {
					tensv.add(rs.getString(2));
					masvl.add(rs.getString(1));
				}
				statement.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			textngayvay.setText(dngayvay);
			textsotien.setText(dsotien);
			comboxTen.addItem(dmasv+"- "+dten);;
			comboBox.addItem(dnganhang);;
		}
		else {
			ten = new Vector<String>();
			ma = new Vector<String>();
			try {
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery("SELECT * FROM nganhang");
				while (rs.next()) {
					ten.add(rs.getString(2));
					ma.add(rs.getString(1));
					comboBox.addItem(rs.getString(2));
				}
				rs = statement.executeQuery("SELECT * FROM sinhvien");
				while (rs.next()) {
					comboxTen.addItem(rs.getString(1)+"- "+rs.getString(2));
				}
				statement.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		JButton btnok = new JButton("OK");
		btnok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ngayvay = textngayvay.getText();
				String sotien = textsotien.getText();
				try {
					final java.sql.Date date = new ConvertDate(ngayvay).convert();
					if (check==1) {
						int index = ten.indexOf(dnganhang);
						String manh = ma.get(index);
						int indexten = tensv.indexOf(dten);
						String masv = masvl.get(indexten);	
						if (ngayvay.equals("") || sotien.equals("")) {
							JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ");
						} else {
							try {
								Double.parseDouble(sotien);
								Statement statement1 = conn.createStatement();
								hoso h = new hoso(date,sotien,manh,masv);
								statement1.executeUpdate("UPDATE `hoso` set  ngayvay='"+h.getNgayvay()+"', sotien='"+h.getSotien()+"' "
										+ "where manh='"+manh+"' AND masv='"+masv+"'");
								statement1.close();
								shs.reloadHoSo();
								shs.modelhs.fireTableDataChanged();
								dispose();
								JOptionPane.showMessageDialog(null, "Sửa thành công!");	
							} catch (NumberFormatException e2) {
								JOptionPane.showMessageDialog(null, "Nhập tiền là một số");
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, "Lỗi, vui lòng nhập lại");
							}
						}
					} else {
						String tennganhang= comboBox.getSelectedItem().toString();
						String tensinhvien= comboxTen.getSelectedItem().toString();
						int index = ten.indexOf(tennganhang);
						String manh = ma.get(index);
						String masv = tensinhvien.substring(0,tensinhvien.indexOf('-'));
						if (ngayvay.equals("") || sotien.equals("")) {
							JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ");
						} else {
							try {
								Double.parseDouble(sotien);
								Statement statement1 = conn.createStatement();
								hoso h = new hoso(date,sotien,manh,masv);
								statement1.executeUpdate("INSERT INTO `hoso` (`masv`, `manh`, `ngayvay`, `sotien`) VALUES ('"+h.getMasv()+"', '"+h.getManh()+"', '"+h.getNgayvay()+"', '"+h.getSotien()+"');");
								statement1.close();
								shs.reloadHoSo();
								shs.modelhs.fireTableDataChanged();
								dispose();
								JOptionPane.showMessageDialog(null, "Chèn thành công!");
							} catch (MySQLIntegrityConstraintViolationException e2) {
								JOptionPane.showMessageDialog(null, "Mỗi sinh viên chỉ vay một ngân hàng");
							} catch (NumberFormatException e3) {
								JOptionPane.showMessageDialog(null, "Nhập tiền là một số");
							} catch (Exception e4) {
								e4.printStackTrace();
								JOptionPane.showMessageDialog(null, "Lỗi, vui lòng nhập lại");
								
							}	
						}
					}
				} catch (ParseException e5) {
					JOptionPane.showMessageDialog(null, "Nhập sai định dạng ngày");
				}
			}
		});
		btnok.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnok.setForeground(new Color(255, 255, 255));
		btnok.setBackground(new Color(64, 157, 250));
		contentPane.add(btnok);
		btnhuy.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnhuy.setForeground(new Color(255, 255, 255));
		btnhuy.setBackground(new Color(64, 157, 250));
		contentPane.add(btnhuy);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
}
