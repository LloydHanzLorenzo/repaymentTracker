/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.repaymenttracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lloyd
 */
public class AlgorithmPage extends javax.swing.JInternalFrame {
    private int loggedInAdminId;

    /**
     * Creates new form AlgorithmPage
     */
    public AlgorithmPage(int adminId) {
        this.loggedInAdminId = adminId;
        initComponents();

        iR3MonthsTextField.setEditable(false);
        iR6MonthsTextField.setEditable(false);
        iR12MonthsTextField.setEditable(false);
        iR24MonthsTextField.setEditable(false);
        updateDateFormattedTextField.setEditable(false);

        loadCurrentInterestRates();
        updateDateFormattedTextField.setText(java.time.LocalDate.now().toString());
        
        loadInterestRateHistory();
    }
    
    private void loadCurrentInterestRates() {
        String sql = "SELECT term_months, interest_rate FROM interest_rates";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int term = rs.getInt("term_months");
                double rate = rs.getDouble("interest_rate");

                switch (term) {
                    case 3:
                        iR3MonthsTextField.setText(String.format("%.2f %%", rate));
                        break;
                    case 6:
                        iR6MonthsTextField.setText(String.format("%.2f %%", rate));
                        break;
                    case 12:
                        iR12MonthsTextField.setText(String.format("%.2f %%", rate));
                        break;
                    case 24:
                        iR24MonthsTextField.setText(String.format("%.2f %%", rate));
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load current interest rates.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateRate(Connection conn, int term, double newRate) throws SQLException {
        conn.setAutoCommit(false);

        PreparedStatement pstmtSelect = null;
        PreparedStatement pstmtUpdate = null;
        PreparedStatement pstmtLog = null;
        ResultSet rs = null;

        try {
            String selectSql = "SELECT interest_rate FROM interest_rates WHERE term_months = ?";
            pstmtSelect = conn.prepareStatement(selectSql);
            pstmtSelect.setInt(1, term);
            rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                double oldRate = rs.getDouble("interest_rate");

                String logSql = "INSERT INTO interest_rate_history (term_months, old_interest_rate, new_interest_rate, changed_by_admin_id) VALUES (?, ?, ?, ?)";
                pstmtLog = conn.prepareStatement(logSql);
                pstmtLog.setInt(1, term);
                pstmtLog.setDouble(2, oldRate);
                pstmtLog.setDouble(3, newRate);
                pstmtLog.setInt(4, this.loggedInAdminId);
                pstmtLog.executeUpdate();

                String updateSql = "UPDATE interest_rates SET interest_rate = ? WHERE term_months = ?";
                pstmtUpdate = conn.prepareStatement(updateSql);
                pstmtUpdate.setDouble(1, newRate);
                pstmtUpdate.setInt(2, term);
                pstmtUpdate.executeUpdate();

                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }        
            if (rs != null) {
                rs.close();
            }
            if (pstmtSelect != null) {
                pstmtSelect.close();
            }
            if (pstmtUpdate != null) {
                pstmtUpdate.close();
            }
            if (pstmtLog != null) {
                pstmtLog.close();
            }
        }
    }
    
    private void loadInterestRateHistory() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "Admin Email", "Term", "Old Rate", "New Rate"}, 0);

        String sql = "SELECT h.change_date, u.email, h.term_months, h.old_interest_rate, h.new_interest_rate "
                + "FROM interest_rate_history h "
                + "LEFT JOIN users u ON h.changed_by_admin_id = u.user_id "
                + "ORDER BY h.change_date DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getTimestamp("change_date"),
                    rs.getString("email"),
                    rs.getInt("term_months") + " Months",
                    rs.getDouble("old_interest_rate") + " %",
                    rs.getDouble("new_interest_rate") + " %"
                });
            }
            jTable1.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        searchTableButton = new javax.swing.JButton();
        searchDateFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        iR6MonthsTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        iR3MonthsTextField = new javax.swing.JTextField();
        iR12MonthsTextField = new javax.swing.JTextField();
        iR24MonthsTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        updateIRButton = new javax.swing.JButton();
        UpdateInterestR6MonthsTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        UpdateInterestR3MonthsTextField = new javax.swing.JTextField();
        UpdateInterestR12MonthsTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        UpdateInterestR24MonthsTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        updateDateFormattedTextField = new javax.swing.JFormattedTextField();
        loadTableButton = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(1250, 610));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        searchTableButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchTableButton.setText("Search");
        searchTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTableButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Interest Rate History");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Current Interest Rate For 3 Months");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Current Interest Rate For 6 Months");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Current Interest Rate For 24 Months");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Current Interest Rate For 12 Months");

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        updateIRButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updateIRButton.setText("Update");
        updateIRButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateIRButtonActionPerformed(evt);
            }
        });

        UpdateInterestR6MonthsTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateInterestR6MonthsTextFieldActionPerformed(evt);
            }
        });
        UpdateInterestR6MonthsTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                UpdateInterestR6MonthsTextFieldKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Update Interest Rate For 3 Months");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Update Interest Rate For 6 Months");

        UpdateInterestR3MonthsTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateInterestR3MonthsTextFieldActionPerformed(evt);
            }
        });
        UpdateInterestR3MonthsTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                UpdateInterestR3MonthsTextFieldKeyReleased(evt);
            }
        });

        UpdateInterestR12MonthsTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateInterestR12MonthsTextFieldActionPerformed(evt);
            }
        });
        UpdateInterestR12MonthsTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                UpdateInterestR12MonthsTextFieldKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Update Interest Rate For 12 Months");

        UpdateInterestR24MonthsTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateInterestR24MonthsTextFieldActionPerformed(evt);
            }
        });
        UpdateInterestR24MonthsTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                UpdateInterestR24MonthsTextFieldKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Update Interest Rate For 24 Months");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Current Date");

        updateDateFormattedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateDateFormattedTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(updateIRButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(UpdateInterestR6MonthsTextField)
                            .addComponent(UpdateInterestR3MonthsTextField)
                            .addComponent(UpdateInterestR12MonthsTextField)
                            .addComponent(UpdateInterestR24MonthsTextField)
                            .addComponent(jLabel10)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7)
                            .addComponent(updateDateFormattedTextField))))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(UpdateInterestR3MonthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UpdateInterestR6MonthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addGap(4, 4, 4)
                .addComponent(UpdateInterestR12MonthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UpdateInterestR24MonthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateDateFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(updateIRButton)
                .addGap(69, 69, 69))
        );

        loadTableButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        loadTableButton.setText("Load");
        loadTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadTableButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchDateFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchTableButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(loadTableButton)
                        .addGap(0, 344, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(iR3MonthsTextField, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(iR6MonthsTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(iR12MonthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(iR24MonthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 59, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchTableButton)
                            .addComponent(searchDateFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(loadTableButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(iR3MonthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(iR6MonthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(iR12MonthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(iR24MonthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(79, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1252, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateIRButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateIRButtonActionPerformed
        String rate3Str = UpdateInterestR3MonthsTextField.getText().trim();
        String rate6Str = UpdateInterestR6MonthsTextField.getText().trim();
        String rate12Str = UpdateInterestR12MonthsTextField.getText().trim();
        String rate24Str = UpdateInterestR24MonthsTextField.getText().trim();

        if (rate3Str.isEmpty() && rate6Str.isEmpty() && rate12Str.isEmpty() && rate24Str.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter at least one new interest rate to update.", "No Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (!rate3Str.isEmpty()) {
                updateRate(conn, 3, Double.parseDouble(rate3Str));
            }
            if (!rate6Str.isEmpty()) {
                updateRate(conn, 6, Double.parseDouble(rate6Str));
            }
            if (!rate12Str.isEmpty()) {
                updateRate(conn, 12, Double.parseDouble(rate12Str));
            }
            if (!rate24Str.isEmpty()) {
                updateRate(conn, 24, Double.parseDouble(rate24Str));
            }

            JOptionPane.showMessageDialog(this, "Interest rates updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            loadCurrentInterestRates();
            loadInterestRateHistory();

            UpdateInterestR3MonthsTextField.setText("");
            UpdateInterestR6MonthsTextField.setText("");
            UpdateInterestR12MonthsTextField.setText("");
            UpdateInterestR24MonthsTextField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for the interest rates.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "A database error occurred while updating rates.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updateIRButtonActionPerformed

    private void UpdateInterestR6MonthsTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateInterestR6MonthsTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UpdateInterestR6MonthsTextFieldActionPerformed

    private void UpdateInterestR3MonthsTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateInterestR3MonthsTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UpdateInterestR3MonthsTextFieldActionPerformed

    private void UpdateInterestR12MonthsTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateInterestR12MonthsTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UpdateInterestR12MonthsTextFieldActionPerformed

    private void UpdateInterestR24MonthsTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateInterestR24MonthsTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UpdateInterestR24MonthsTextFieldActionPerformed

    private void UpdateInterestR3MonthsTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UpdateInterestR3MonthsTextFieldKeyReleased
        
    }//GEN-LAST:event_UpdateInterestR3MonthsTextFieldKeyReleased

    private void updateDateFormattedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateDateFormattedTextFieldActionPerformed
        updateDateFormattedTextField.setEditable(false);
    }//GEN-LAST:event_updateDateFormattedTextFieldActionPerformed

    private void UpdateInterestR6MonthsTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UpdateInterestR6MonthsTextFieldKeyReleased
        
    }//GEN-LAST:event_UpdateInterestR6MonthsTextFieldKeyReleased

    private void UpdateInterestR12MonthsTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UpdateInterestR12MonthsTextFieldKeyReleased
        
    }//GEN-LAST:event_UpdateInterestR12MonthsTextFieldKeyReleased

    private void UpdateInterestR24MonthsTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UpdateInterestR24MonthsTextFieldKeyReleased
        
    }//GEN-LAST:event_UpdateInterestR24MonthsTextFieldKeyReleased

    private void searchTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTableButtonActionPerformed
        String keyword = searchDateFormattedTextField.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String termSearchKeyword = keyword.toLowerCase().replace("months", "").trim();

        DefaultTableModel model = new DefaultTableModel(new String[]{"Date", "Admin Email", "Term", "Old Rate", "New Rate"}, 0);

        String sql = "SELECT h.change_date, u.email, h.term_months, h.old_interest_rate, h.new_interest_rate "
                + "FROM interest_rate_history h "
                + "LEFT JOIN users u ON h.changed_by_admin_id = u.user_id "
                + "WHERE CAST(h.change_date AS CHAR) LIKE ? "
                + "OR u.email LIKE ? "
                + "OR CAST(h.term_months AS CHAR) LIKE ? "
                + "ORDER BY h.change_date DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            String termSearchPattern = "%" + termSearchKeyword + "%";

            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, termSearchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    JOptionPane.showMessageDialog(this, "No history found matching your search.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                    jTable1.setModel(model);
                } else {
                    while (rs.next()) {
                        model.addRow(new Object[]{
                            rs.getTimestamp("change_date"),
                            rs.getString("email"),
                            rs.getInt("term_months") + " Months",
                            rs.getDouble("old_interest_rate") + " %",
                            rs.getDouble("new_interest_rate") + " %"
                        });
                    }
                    jTable1.setModel(model);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Search failed due to a database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_searchTableButtonActionPerformed

    private void loadTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadTableButtonActionPerformed
        loadInterestRateHistory();
        searchDateFormattedTextField.setText("");
    }//GEN-LAST:event_loadTableButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField UpdateInterestR12MonthsTextField;
    private javax.swing.JTextField UpdateInterestR24MonthsTextField;
    private javax.swing.JTextField UpdateInterestR3MonthsTextField;
    private javax.swing.JTextField UpdateInterestR6MonthsTextField;
    private javax.swing.JTextField iR12MonthsTextField;
    private javax.swing.JTextField iR24MonthsTextField;
    private javax.swing.JTextField iR3MonthsTextField;
    private javax.swing.JTextField iR6MonthsTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton loadTableButton;
    private javax.swing.JFormattedTextField searchDateFormattedTextField;
    private javax.swing.JButton searchTableButton;
    private javax.swing.JFormattedTextField updateDateFormattedTextField;
    private javax.swing.JButton updateIRButton;
    // End of variables declaration//GEN-END:variables
}
