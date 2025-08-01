/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repaymenttracker;

import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author seans
 */
public class PasswordHasher {

    public static void main(String[] args) {
        String plainPassword = "adminpass"; 
        
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        
        System.out.println("Plain Password: " + plainPassword);
        System.out.println("Hashed Password: " + hashedPassword);
    }
}