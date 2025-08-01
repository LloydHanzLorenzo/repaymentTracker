-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 01, 2025 at 12:52 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `borrowerdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `borrowers`
--

CREATE TABLE `borrowers` (
  `borrower_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) NOT NULL,
  `birthdate` date DEFAULT NULL,
  `contact_number` varchar(20) DEFAULT NULL,
  `nationality` varchar(50) DEFAULT NULL,
  `employment_status` varchar(50) DEFAULT NULL,
  `monthly_income` decimal(10,2) DEFAULT NULL,
  `home_address` text DEFAULT NULL,
  `valid_id_number` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrowers`
--

INSERT INTO `borrowers` (`borrower_id`, `user_id`, `first_name`, `middle_name`, `last_name`, `birthdate`, `contact_number`, `nationality`, `employment_status`, `monthly_income`, `home_address`, `valid_id_number`) VALUES
(1, 2, 'John', 'A', 'Doe', '1990-05-15', '09171234567', 'Filipino', 'Employed', 35000.00, '123 Maple Street, Quezon', 'ID-123456789'),
(2, 3, 'Jane', 'B', 'Smith', '1992-11-20', '09289876543', 'Filipino', 'Self-Employed', 52000.00, '456 Oak Avenue, Makati City', 'ID-987654321'),
(3, 4, 'Matthew', 'Cadlum', 'Mesia', '0001-01-01', '09999999', NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `interest_rates`
--

CREATE TABLE `interest_rates` (
  `term_months` int(11) NOT NULL,
  `interest_rate` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `interest_rates`
--

INSERT INTO `interest_rates` (`term_months`, `interest_rate`) VALUES
(3, 3.50),
(6, 6.75),
(12, 13.50),
(24, 18.00);

-- --------------------------------------------------------

--
-- Table structure for table `interest_rate_history`
--

CREATE TABLE `interest_rate_history` (
  `history_id` int(11) NOT NULL,
  `term_months` int(11) NOT NULL,
  `old_interest_rate` decimal(5,2) NOT NULL,
  `new_interest_rate` decimal(5,2) NOT NULL,
  `changed_by_admin_id` int(11) DEFAULT NULL,
  `change_date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `interest_rate_history`
--

INSERT INTO `interest_rate_history` (`history_id`, `term_months`, `old_interest_rate`, `new_interest_rate`, `changed_by_admin_id`, `change_date`) VALUES
(1, 3, 3.50, 3.60, 1, '2025-08-01 18:12:54'),
(2, 6, 6.75, 6.90, 1, '2025-08-01 18:12:54'),
(3, 12, 13.50, 13.70, 1, '2025-08-01 18:12:54'),
(4, 24, 18.00, 18.25, 1, '2025-08-01 18:12:54'),
(5, 3, 3.60, 3.50, 1, '2025-08-01 18:13:42'),
(6, 6, 6.90, 6.75, 1, '2025-08-01 18:13:42'),
(7, 12, 13.70, 13.50, 1, '2025-08-01 18:13:42'),
(8, 24, 18.25, 18.00, 1, '2025-08-01 18:13:42'),
(9, 3, 3.50, 3.50, 1, '2025-08-01 18:30:39'),
(10, 6, 6.75, 6.75, 1, '2025-08-01 18:30:39'),
(11, 12, 13.50, 13.50, 1, '2025-08-01 18:30:39'),
(12, 24, 18.00, 18.00, 1, '2025-08-01 18:30:39');

-- --------------------------------------------------------

--
-- Table structure for table `loans`
--

CREATE TABLE `loans` (
  `loan_id` int(11) NOT NULL,
  `borrower_id` int(11) NOT NULL,
  `loan_amount` decimal(12,2) NOT NULL,
  `loan_purpose` text DEFAULT NULL,
  `loan_status` enum('Pending','Approved','Rejected','Paid','Defaulted') DEFAULT 'Pending',
  `application_date` datetime DEFAULT current_timestamp(),
  `approval_date` datetime DEFAULT NULL,
  `repayment_term_months` int(11) NOT NULL,
  `interest_rate_at_approval` decimal(5,2) NOT NULL,
  `notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `loans`
--

INSERT INTO `loans` (`loan_id`, `borrower_id`, `loan_amount`, `loan_purpose`, `loan_status`, `application_date`, `approval_date`, `repayment_term_months`, `interest_rate_at_approval`, `notes`) VALUES
(101, 1, 15000.00, 'Emergency medical expenses', 'Approved', '2025-06-01 10:00:00', '2025-06-02 11:30:00', 6, 6.75, 'Urgent request, verified income.'),
(102, 1, 5000.00, 'Phone repair', 'Paid', '2024-01-10 14:20:00', '2024-01-11 09:00:00', 3, 3.50, 'Good payment history.'),
(201, 2, 50000.00, 'Small business capital', 'Approved', '2025-07-20 09:15:00', '2025-08-01 14:31:09', 24, 13.50, 'Applicant submitted business permit.\nLoan rescheduled by admin.'),
(202, 2, 10000.00, 'Travel funds', 'Rejected', '2025-03-05 17:00:00', NULL, 3, 3.50, 'Reason for rejection: Non-essential loan purpose.'),
(203, 1, 10000.00, 'Starting a business or smth', 'Approved', '2025-08-01 01:18:05', '2025-06-02 11:30:00', 24, 18.00, '\nLoan rescheduled by admin.\nLoan rescheduled by admin.');

-- --------------------------------------------------------

--
-- Table structure for table `repayments`
--

CREATE TABLE `repayments` (
  `repayment_id` int(11) NOT NULL,
  `loan_id` int(11) NOT NULL,
  `payment_date` date NOT NULL,
  `amount_paid` decimal(10,2) NOT NULL,
  `notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `repayments`
--

INSERT INTO `repayments` (`repayment_id`, `loan_id`, `payment_date`, `amount_paid`, `notes`) VALUES
(1, 101, '2025-07-01', 2500.00, 'First monthly payment.'),
(2, 101, '2025-07-30', 2500.00, 'Second monthly payment.'),
(3, 102, '2024-02-10', 1750.00, 'Payment 1'),
(4, 102, '2024-03-10', 1750.00, 'Payment 2'),
(5, 102, '2024-04-09', 1750.00, 'Final payment.'),
(6, 201, '2025-06-01', 10000.00, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` enum('admin','user') NOT NULL DEFAULT 'user',
  `created_at` datetime DEFAULT current_timestamp(),
  `account_status` enum('Active','Deactivated') NOT NULL DEFAULT 'Active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `password_hash`, `role`, `created_at`, `account_status`) VALUES
(1, 'admin@repayright.com', '$2a$10$N.TgyCyBQyPIM1hsDoyhtOD9nLKcj9XXAvJYmtYCGCd76D94PvnBS', 'admin', '2025-07-31 22:05:58', 'Active'),
(2, 'john.doe@email.com', '$2a$10$v7R5XlCpd5LeM9uSDvD6xOydRG5O6n..5eqprKvtUpNV3f2/xNTha', 'user', '2025-07-31 22:05:58', 'Active'),
(3, 'jane.smith@email.com', '$2a$10$0CrwpYTB6I7f2ba6jOPdm.VGX0BaTSWytDClWaygvgGfemQKjMLgC', 'user', '2025-07-31 22:05:58', 'Active'),
(4, 'matthew@mesia.com', '$2a$10$rF61SXNN3b3x6/Qor8DU7u7BTHFX/aC.EsZvTgPPpT4M/vRqZai4y', 'user', '2025-07-31 23:34:37', 'Active'),
(5, 'notnew@admin.com', '$2a$10$IDcIZjgqVqLRXgL4MaWLwevN0BCn5ZYLtkVaIUyJVZdiCBryX3PgO', 'admin', '2025-08-01 04:02:09', 'Active');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `borrowers`
--
ALTER TABLE `borrowers`
  ADD PRIMARY KEY (`borrower_id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `interest_rates`
--
ALTER TABLE `interest_rates`
  ADD PRIMARY KEY (`term_months`);

--
-- Indexes for table `interest_rate_history`
--
ALTER TABLE `interest_rate_history`
  ADD PRIMARY KEY (`history_id`),
  ADD KEY `changed_by_admin_id` (`changed_by_admin_id`);

--
-- Indexes for table `loans`
--
ALTER TABLE `loans`
  ADD PRIMARY KEY (`loan_id`),
  ADD KEY `borrower_id` (`borrower_id`),
  ADD KEY `repayment_term_months` (`repayment_term_months`);

--
-- Indexes for table `repayments`
--
ALTER TABLE `repayments`
  ADD PRIMARY KEY (`repayment_id`),
  ADD KEY `loan_id` (`loan_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `borrowers`
--
ALTER TABLE `borrowers`
  MODIFY `borrower_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `interest_rate_history`
--
ALTER TABLE `interest_rate_history`
  MODIFY `history_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `loans`
--
ALTER TABLE `loans`
  MODIFY `loan_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=204;

--
-- AUTO_INCREMENT for table `repayments`
--
ALTER TABLE `repayments`
  MODIFY `repayment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `borrowers`
--
ALTER TABLE `borrowers`
  ADD CONSTRAINT `borrowers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `interest_rate_history`
--
ALTER TABLE `interest_rate_history`
  ADD CONSTRAINT `interest_rate_history_ibfk_1` FOREIGN KEY (`changed_by_admin_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `loans`
--
ALTER TABLE `loans`
  ADD CONSTRAINT `loans_ibfk_1` FOREIGN KEY (`borrower_id`) REFERENCES `borrowers` (`borrower_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `loans_ibfk_2` FOREIGN KEY (`repayment_term_months`) REFERENCES `interest_rates` (`term_months`);

--
-- Constraints for table `repayments`
--
ALTER TABLE `repayments`
  ADD CONSTRAINT `repayments_ibfk_1` FOREIGN KEY (`loan_id`) REFERENCES `loans` (`loan_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
