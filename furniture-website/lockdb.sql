-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 26, 2022 at 08:08 AM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 7.4.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lockdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

CREATE TABLE `address` (
  `id` int(11) NOT NULL,
  `userID` int(11) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `address`
--

INSERT INTO `address` (`id`, `userID`, `address`, `city`, `country`, `updated`) VALUES
(45, 57, 'Tsar Boris III 271', 'Sofia', 'AL', '2022-06-25 09:10:11'),
(70, 83, 'Tsar Boris III 271', 'Sofia', 'AF', '2022-06-25 20:52:27'),
(71, 84, 'Tsar Boris III 271', 'Sofia', 'AD', '2022-06-25 20:53:31'),
(72, 85, 'Tsar Boris III 271', 'Sofia', 'AX', '2022-06-25 21:58:46');

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `id` int(11) NOT NULL,
  `userID` int(11) DEFAULT NULL,
  `total` float DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`id`, `userID`, `total`, `updated`) VALUES
(46, 57, NULL, '2022-06-25 07:15:37');

-- --------------------------------------------------------

--
-- Table structure for table `cart_product`
--

CREATE TABLE `cart_product` (
  `id` int(11) NOT NULL,
  `cartID` int(11) DEFAULT NULL,
  `productID` int(11) DEFAULT NULL,
  `quantity` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `order_`
--

CREATE TABLE `order_` (
  `id` int(11) NOT NULL,
  `userID` int(11) DEFAULT NULL,
  `addressID` int(11) DEFAULT NULL,
  `total` float DEFAULT NULL,
  `status` int(11) NOT NULL,
  `ship_method` varchar(255) NOT NULL,
  `pay_method` varchar(255) NOT NULL,
  `created` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `order_`
--

INSERT INTO `order_` (`id`, `userID`, `addressID`, `total`, `status`, `ship_method`, `pay_method`, `created`) VALUES
(48, 83, 70, 270, 0, '0', '1', '2022-06-25 23:52:27'),
(49, 84, 71, 270, 0, '0', '1', '2022-06-25 23:53:31'),
(50, 85, 72, 390, 0, '2', '2', '2022-06-26 00:58:47');

-- --------------------------------------------------------

--
-- Table structure for table `order_product`
--

CREATE TABLE `order_product` (
  `id` int(11) NOT NULL,
  `orderID` int(11) DEFAULT NULL,
  `productID` int(11) DEFAULT NULL,
  `quantity` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `order_product`
--

INSERT INTO `order_product` (`id`, `orderID`, `productID`, `quantity`) VALUES
(8, 48, 5, 1),
(9, 49, 5, 1),
(10, 50, 4, 1);

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `oldprice` int(11) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `series` varchar(100) DEFAULT NULL,
  `dimensions` varchar(100) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `material` varchar(1000) DEFAULT NULL,
  `color` varchar(100) DEFAULT NULL,
  `dir` varchar(100) DEFAULT NULL,
  `shipping` tinyint(1) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `name`, `price`, `oldprice`, `type`, `series`, `dimensions`, `weight`, `material`, `color`, `dir`, `shipping`, `keywords`) VALUES
(3, 'Lesse Chair', 360, 460, 'chair', 'lesse', '79x49x53', 6.2, 'beech wood legs, 18mm beech plywood construction, seating and backrest - 4mm polycarbonate, finished with oil.', 'natural', 'assets/img/furniture/chair/1_Lese_Chair_Natural', 0, 'lesse chair natural-color '),
(4, 'Lesse Chair | Black', 390, NULL, 'chair', 'lesse', '79x49x53', 6.2, 'beech wood legs, 18mm beech plywood construction, seating and backrest - 4mm polycarbonate, finished with black stain oil.', 'black', 'assets/img/furniture/chair/2_Lese_Chair_Black_stain', 1, 'lesse chair black '),
(5, 'Frame Armchair', 270, NULL, 'chair', 'frame', '81x56x52', 6.2, '18mm beech plywood, finished with oil', 'natural', 'assets/img/furniture/chair/3_Frame_Arm_Chair_Natural', 1, 'frame chair armchair natural-color'),
(10, 'Frame Armchair | Black', 290, NULL, 'chair', 'frame', '81X56X52', 6.2, '18 mm beech plywood, finished with black stain oil.', 'black', 'assets/img/furniture/chair/4_Frame_arm_chair_black', 0, 'frame chair black arm'),
(11, 'Electron Chair | Natural Color | Texttile', 290, 0, 'chair', NULL, '82X51X50', 6, 'Beech plywood 18 мм, oiled. UV protected, coated textile', 'natural', 'assets/img/furniture/chair/7_Electron_natural_textile', 1, 'chair electron natural-color award'),
(13, 'Electron Chair | Black | Leather', 490, NULL, 'chair', NULL, ' 82X51X50', 6, 'Beech plywood 18mm, natural leather', 'black', 'assets/img/furniture/chair/10_Electron_black_stain_Leather', 0, 'leather black electron chair award'),
(14, 'Arch Chair | Leather', 490, NULL, 'chair', 'arch', '77X58X54', 6, '18 mm beech plywood', 'natural', 'assets/img/furniture/chair/12_Arch_chair_natural_colour_oiled_leather', 1, 'chair arch natural-color leather'),
(15, 'Arch Chair | Stain | Leather', 490, NULL, 'chair', 'arch', '77X58X54', 6, '18 mm beech plywood. Seating and backrest – leather', 'stain', 'assets/img/furniture/chair/14_Arch_chair_different_colours_oiled_leather', 1, 'chair arch blue leather'),
(16, 'Arch Armchair | Leather', 550, NULL, 'chair', 'arch', '81X68X56', 6, '18 mm beech plywood. Seating - natural leather, backrest – upholstered.', 'natural', 'assets/img/furniture/chair/18_Arch_Ar_chair_different_colours_oiled_leather', 0, 'chair arch arm natural-color leather'),
(17, 'Lesse Lowhair', 430, NULL, 'chair', 'arch', '74X50X56', 7, 'beech wood legs, 18mm beech plywood construction, seating and backrest - 4mm polycarbonate, finished with oil.', 'natural', 'assets/img/furniture/chair/19_Lese_lowchair natural_colour_oiled', 0, 'chair lesse low natural-color'),
(18, 'Arch Lowhair | Leather', 318, NULL, 'chair', 'arch', '77X75X60', 7, '18 mm beech plywood construction, seating - textile or leather, backrest – upholstered.', 'natural', 'assets/img/furniture/chair/22_Arch_lowchair_natural_colour_oiled_leather', 1, 'chair arch low natural-color'),
(23, 'Arch Lowchair | Stain | Textile', 318, NULL, 'chair', 'arch', '77X75X60', 7, '18 mm beech plywood construction, seating - textile or leather, backrest – upholstered.', 'stain', 'assets/img/furniture/chair/23_Arch_lowchair_different_colours_oiled_textile', 1, 'chair arch low stain textile'),
(24, 'Arch Lowchair | Black | Leather', 498, NULL, 'chair', 'arch', '77X75X60', 7, '18 mm beech plywood construction, seating - textile or leather, backrest – upholstered.', 'black', 'assets/img/furniture/chair/24_Arch_lowchair_different_colours_oiled_leather', 1, 'chair arch low black leather'),
(25, 'Lesse Stool', 210, NULL, 'stool', 'lesse', ' 47X36X36', 4.4, ' beech wood legs, consrtuction from 18mm beech plywood, seating from 4mm polycarbonate, finished with oil', 'natural', 'assets/img/furniture/chair/29_Lese_stool_natural_colour_oiled', 0, 'lesse stool natural-color  sidettable chair'),
(26, 'Lesse Stool | Black', 230, NULL, 'stool', 'lesse', ' 47X36X36', 4.4, 'beech wood legs, consrtuction from 18mm beech plywood, seating from 4mm polycarbonate, finished with black stain oil', 'black', 'assets/img/furniture/chair/30_Lese_stool_different_colours_oiled', 0, 'lesse stool black  sidetable chair'),
(27, 'Space Sofa', 3000, NULL, 'sofa', 'space', ' 75X194X87', 30, '30 mm beech plywood, 18 mm beech plywood, 4 mm polycarbonate, Seating and backrest – upholstered.', 'natural', 'assets/img/furniture/chair/39_Space_sofa_natural_colour_oiled_textile', 1, 'space sofa natural-color '),
(28, 'Android Chair | Black', 238, NULL, 'chair', 'android', '80X49X53', 6.2, '18 mm beech plywood, finished with black stain oil.', 'black', 'assets/img/furniture/chair/Android', 0, 'android chair black'),
(29, 'Arch Barstoo | Blackl', 490, NULL, 'barstool', 'arch', '64X54X42', 5, '18 mm beech plywood, finished with oil, natural leather seating', 'black', 'assets/img/furniture/chair/Arch_bar_stool', 0, 'arch barstol black leather'),
(34, 'Arch Lowchair | Black | Textile', 318, NULL, 'chair', 'arch', '77X75X60', 7, '18 mm beech plywood construction, seating - textile or leather, backrest – upholstered.', 'stain', 'assets/img/furniture/chair/arch_lowchair_different_colours_oiled_TWO', 1, 'arch chair low stain textile'),
(35, 'Colibri Stool', 77, NULL, 'stool', NULL, '30X30X40', 3.2, 'Beech plywood, 18 mm, finished with bio oil', 'natural', 'assets/img/furniture/chair/Colibri-white', 1, 'natural-color stool colibri'),
(36, 'Stack Barstool | Black', 180, NULL, 'barstool', 'stack', '78X25X30', 5.5, '18 mm beech plywood, finished with black stain oil', 'black', 'assets/img/furniture/chair/Stack_bar_stool', 0, 'chair barstool stack black'),
(37, 'Lesse Lamp', 273, NULL, 'lamp', 'series', ' 160X38X38', 5.8, 'Beech plywood 18mm, beech hardwood legs', 'natural', 'assets/img/furniture/lamp/Leses_lamps/79_Lese_floor_lamp_natural_colour_oiled', 1, 'natural lamp lesse floor'),
(38, 'Antenna Pendant', 40, NULL, 'lamp', 'pendant', '26X30X10', 0.6, 'Birch plywood Cable: 2-meter fabric cable included Light source: E27 mount, lighting bulb not included Watts: Energy saving LED light bulbs or regular', 'natural', 'assets/img/furniture/lamp/Pendants/Antenna', 1, 'pendant lamp natural-color hanging'),
(39, 'Chan Pendant | 1', 40, NULL, 'lamp', 'pendant', '30X60X30', 0.8, ' Birch plywood, Cable: 2-meter fabric cable included, Light source: E27 mount, lighting bulb not included Watts: Energy saving LED light bulbs or regular', 'natural', 'assets/img/furniture/lamp/Pendants/Chan1', 1, 'natural-color lamp pendant hanging'),
(40, 'Chan Pendant | 2', 40, NULL, 'lamp', 'pendant', '30X60X30', 0.8, ' Birch plywood, Cable: 2-meter fabric cable included, Light source: E27 mount, lighting bulb not included Watts: Energy saving LED light bulbs or regular', 'natural', 'assets/img/furniture/lamp/Pendants/Chan2', 1, 'natural-color lamp pendant hanging'),
(41, 'Satellite Pendant', 60, NULL, 'lamp', 'pendant', '26X76X76', 1.4, 'Birch plywood feathers, acrylic rosette. Cable: 2-meter fabric cable included. Light source: E27 mount, lighting bulb not included. Watts: Energy savi', 'natural', 'assets/img/furniture/lamp/Pendants/Satellite', 1, 'natural-color lamp pendant hanging'),
(42, 'Space Coathanger', 85, NULL, 'store', 'space', '172X65X62 ', 5, 'Beech plywood 20mm', 'natural', 'assets/img/furniture/store/66_Space_coat_hanger_natural_colour_oiled', 0, 'space store hanger natural-color'),
(43, 'Space Coathanger | Black', 95, NULL, 'store', 'space', '172X65X62 ', 5, 'Beech plywood 20mm', 'black', 'assets/img/furniture/store/67_Space_coat_hanger_different_colours_oiled', 1, 'space store hanger black'),
(44, 'Fulcrum Shelf', 348, NULL, 'store', NULL, '158X80X35', 20, 'Beech ', 'natural', 'assets/img/furniture/store/68_Fulcrum_shelf_natural_colour_oiled', 1, 'natural-color store fulcrum'),
(45, 'Anchor Coathanger', 25, NULL, 'store', NULL, '14X19X2 ', 0.6, 'beech plywood', 'stain', 'assets/img/furniture/store/Coat_hanger_anchor', 0, 'hanger stain store'),
(46, 'Arch Desk', 590, NULL, 'table', 'arch', '83X120X63', 12, 'Beech', 'natural', 'assets/img/furniture/table/arch-desk', 1, 'arch desk natural-color'),
(47, 'Lesse Lamp | Black', 287, NULL, 'lamp', 'lesse', '160X38X38', 5.8, ' Beech plywood 18mm, beech hardwood legs', 'black', 'assets/img/furniture/lamp/Leses_lamps/80_Lese_floor_lamp_different_colours_oiled', 1, 'black lesse lamp floor'),
(48, 'Arch Bar Table', 790, NULL, 'table', 'arch', '76X80X120', 15, 'Table top – 30 mm beech hardwood, legs - 30 mm beech plywood, construction – 18 mm beech plywood. Finished with BIOFA/Germany/ solvent-free', 'natural', 'assets/img/furniture/table/Bar', 1, 'bar table arch natural-color'),
(49, 'Arch Table', 890, NULL, 'table', 'arch', '76X200X80 ', 20, 'Table top – 30 mm beech hardwood, legs - 30 mm beech plywood, construction – 18 mm beech plywood. Finished with BIOFA/Germany/ solvent-fre', 'natural', 'assets/img/furniture/table/Dining/arch-table', 1, 'table arch natural-color'),
(50, 'Android Table', 490, NULL, 'table', 'android', '78X80X80', 14, 'Beech plywood, 18 mm, finished with bio oil', 'natural', 'assets/img/furniture/table/Dining/android-table', 1, 'natural-color table android'),
(51, 'Space Sidetable', 60, NULL, 'table', 'space', '40X48X48 ', 4, '18mm beech plywood, finished with oil', 'natural', 'assets/img/furniture/table/Side/54_Space_side_table_middle-natural_colour_oiled_plywood', 1, 'space table side natural-color'),
(52, 'Space Sidetable | Black', 75, NULL, 'table', 'space', '40X48X48 ', 4, '18mm beech plywood, finished with oil', 'black', 'assets/img/furniture/table/Side/55_Space_side_table_middle_different_colours_oiled_plywood_black', 1, 'space table side black'),
(53, 'Space Sidetable | Red', 75, NULL, 'table', 'space', '40X48X48 ', 4, '18mm beech plywood, finished with oil', 'red', 'assets/img/furniture/table/Side/55_Space_side_table_middle_different_colours_oiled_plywood_red', 1, 'space table side red'),
(54, 'Vetruvex Table | White Stone', 332, NULL, 'table', 'vetruvex', '47X55X55', 7, ' 18mm beech plywood, finished with bio oil', 'natural', 'assets/img/furniture/table/Side/64_Vetruvex_side_table_natural_stone_white', 1, 'vetruvex table natural-color stone'),
(55, 'Space Sidetable | Dekton Domoos', 270, NULL, 'table', 'space', '40X48X48 ', 7, '18mm beech plywood, finished with oil, tabletop DEKTON', 'natural', 'assets/img/furniture/table/Side/56_Space_side_table_middle_natural_colour_oiled_stone_Dekton', 1, 'natural-color stone space table side '),
(56, 'Space Sidetable | Dekton Zenith', 270, NULL, 'table', 'space', '40X48X48 ', 7, '18mm beech plywood, finished with oil, tabletop DEKTON', 'natural', ' assets/img/furniture/table/Side/56_Space_side_table_middle_natural_colour_oiled_stone_Zenith', 1, 'space table side natural-color stone'),
(57, 'Vetruvex Table', 132, NULL, 'table', 'vetruvex', '47X55X55', 4, ' 18mm beech plywood, finished with bio oil', 'natural', 'assets/img/furniture/table/Side/62_Vetruvex_side_table_natural_colour_oiled_plywood', 1, 'vetruvex table side natural-color'),
(58, 'Vetruvex Table | Black Stone', 332, NULL, 'table', 'vetruvex', '47X55X55', 7, ' 18mm beech plywood, finished with bio oil', 'natural', 'assets/img/furniture/table/Side/64_Vetruvex_side_table_natural_stone_black', 1, 'vetruvex side table natural-color stone');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `fname` varchar(255) DEFAULT NULL,
  `lname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `pass` varchar(1000) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `privacy` tinyint(1) DEFAULT NULL,
  `terms` tinyint(1) DEFAULT NULL,
  `newsletter` tinyint(1) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `fname`, `lname`, `email`, `pass`, `phone`, `privacy`, `terms`, `newsletter`, `created`) VALUES
(57, 'Nikol', 'Georgiev', 'nikola.dey.georgiev@gmail.com', '59ad77093eb559ce4be944e4238383b5', '0882725017', 1, 1, 1, '2022-06-25 07:15:37'),
(83, 'Nikol', 'Georgiev', 'test_email.nikola@abv.bg', NULL, '0882725017', NULL, NULL, NULL, '2022-06-25 20:52:27'),
(84, 'Nikol', 'Georgiev', 'nikola.dey.georgiev@abv.bg', NULL, '0882725017', NULL, NULL, NULL, '2022-06-25 20:53:31'),
(85, 'Nikol', 'Georgiev', 'nikolaa.dey.georgiev@gmail.com', NULL, '0882725017', NULL, NULL, NULL, '2022-06-25 21:58:46');

-- --------------------------------------------------------

--
-- Table structure for table `whishlist`
--

CREATE TABLE `whishlist` (
  `id` int(11) NOT NULL,
  `userID` int(11) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `whishlist`
--

INSERT INTO `whishlist` (`id`, `userID`, `updated`) VALUES
(2, 13, '2022-06-14 06:42:14'),
(3, 13, '2022-06-14 06:43:13'),
(4, 13, '2022-06-14 06:44:24'),
(5, 13, '2022-06-14 06:46:13'),
(6, 13, '2022-06-14 14:55:36'),
(7, 13, '2022-06-14 14:57:18'),
(8, 13, '2022-06-14 14:57:42'),
(9, 20, '2022-06-14 15:11:14'),
(10, 20, '2022-06-14 15:11:22'),
(11, 20, '2022-06-14 15:12:04'),
(12, 23, '2022-06-15 15:30:32'),
(13, 24, '2022-06-15 16:15:57'),
(14, 25, '2022-06-15 16:29:17'),
(15, 26, '2022-06-15 17:34:20'),
(16, 27, '2022-06-20 17:12:44'),
(17, 28, '2022-06-20 17:16:57'),
(18, 29, '2022-06-21 05:16:56'),
(19, 30, '2022-06-21 05:24:04'),
(20, 31, '2022-06-21 05:37:02'),
(21, 32, '2022-06-21 05:40:44'),
(22, 33, '2022-06-21 05:44:39'),
(23, 34, '2022-06-21 05:52:51'),
(24, 35, '2022-06-24 20:35:27'),
(25, 36, '2022-06-24 22:20:52'),
(26, 37, '2022-06-24 22:28:09'),
(27, 38, '2022-06-24 22:33:44'),
(28, 39, '2022-06-24 22:36:33'),
(29, 40, '2022-06-24 22:36:52'),
(30, 41, '2022-06-24 22:37:15'),
(31, 42, '2022-06-24 22:38:32'),
(32, 43, '2022-06-24 22:39:42'),
(33, 44, '2022-06-24 22:50:41'),
(34, 45, '2022-06-24 22:51:42'),
(35, 46, '2022-06-24 22:52:08'),
(36, 47, '2022-06-24 22:52:48'),
(37, 48, '2022-06-24 22:54:36'),
(38, 49, '2022-06-24 22:56:10'),
(39, 50, '2022-06-24 22:56:44'),
(40, 51, '2022-06-24 22:57:11'),
(41, 52, '2022-06-24 22:57:39'),
(42, 53, '2022-06-24 23:00:14'),
(43, 54, '2022-06-24 23:02:09'),
(44, 55, '2022-06-24 23:02:48'),
(45, 56, '2022-06-25 07:13:15'),
(46, 57, '2022-06-25 07:15:37');

-- --------------------------------------------------------

--
-- Table structure for table `whishlist_product`
--

CREATE TABLE `whishlist_product` (
  `id` int(11) NOT NULL,
  `whishlistID` int(11) DEFAULT NULL,
  `productID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `address`
--
ALTER TABLE `address`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cart_product`
--
ALTER TABLE `cart_product`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order_`
--
ALTER TABLE `order_`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order_product`
--
ALTER TABLE `order_product`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `whishlist`
--
ALTER TABLE `whishlist`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `whishlist_product`
--
ALTER TABLE `whishlist_product`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `address`
--
ALTER TABLE `address`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `cart_product`
--
ALTER TABLE `cart_product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `order_`
--
ALTER TABLE `order_`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `order_product`
--
ALTER TABLE `order_product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=86;

--
-- AUTO_INCREMENT for table `whishlist`
--
ALTER TABLE `whishlist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `whishlist_product`
--
ALTER TABLE `whishlist_product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
