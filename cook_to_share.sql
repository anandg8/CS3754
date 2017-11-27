-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 27, 2017 at 12:13 AM
-- Server version: 5.7.14
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cook_to_share`
--

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT 'user_id of the commenter',
  `reply_id` int(11) NOT NULL COMMENT 'reply_id, Foreign key that points back to this table. Field can be left blank',
  `comment_table_id` int(11) NOT NULL,
  `date_posted` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_visible` int(11) NOT NULL DEFAULT '1' COMMENT 'Is the comment visible or ''deleted''',
  `vote_id` int(11) NOT NULL COMMENT 'Points to the vote id on the vote table',
  `comment` varchar(500) NOT NULL COMMENT 'Comment itself'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `comments_conjunction`
--

CREATE TABLE `comments_conjunction` (
  `id` int(11) NOT NULL,
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `comments_conjunction`
--

INSERT INTO `comments_conjunction` (`id`, `date_created`) VALUES
(1, '2017-11-21 13:18:03');

-- --------------------------------------------------------

--
-- Table structure for table `cuisine_type`
--

CREATE TABLE `cuisine_type` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL COMMENT 'Name of Cusine',
  `description` varchar(500) NOT NULL COMMENT 'Description of Cusine'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cuisine_type`
--

INSERT INTO `cuisine_type` (`id`, `name`, `description`) VALUES
(1, 'Chinese', 'Traditional Chinese Cooking'),
(2, 'Indian', 'Traditional Indian Cooking');

-- --------------------------------------------------------

--
-- Table structure for table `dish`
--

CREATE TABLE `dish` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT 'user_id Foreign Key',
  `cuisine_id` int(11) NOT NULL COMMENT 'cuisine_id Foreign Key',
  `dish_name` varchar(15) NOT NULL,
  `description` text NOT NULL COMMENT 'description of dish',
  `dish_picture_path` varchar(200) NOT NULL,
  `reservation_time` datetime NOT NULL COMMENT 'reservation time',
  `meal_time` datetime NOT NULL COMMENT 'meal time, != reservation time',
  `cost` double UNSIGNED NOT NULL COMMENT 'Cost of meal represented as: XX.XX',
  `num_guests` int(10) UNSIGNED NOT NULL COMMENT 'Number of Guests: Cannot be negative',
  `comments_id` int(11) NOT NULL COMMENT 'comments_key Foreign Key'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dish`
--

INSERT INTO `dish` (`id`, `user_id`, `cuisine_id`, `dish_name`, `description`, `dish_picture_path`, `reservation_time`, `meal_time`, `cost`, `num_guests`, `comments_id`) VALUES
(1, 1, 1, 'General Tso', 'Chicken Dish\r\nVery Healthy', 'http://www.seriouseats.com/recipes/assets_c/2015/04/20140328-general-tsos-chicken-recipe-food-lab-1-thumb-1500xauto-422309.jpg', '2017-11-28 15:00:00', '2017-11-28 18:00:00', 5.5, 5, 1);

-- --------------------------------------------------------

--
-- Table structure for table `friends`
--

CREATE TABLE `friends` (
  `user_id_one` int(11) NOT NULL COMMENT 'First friend user id',
  `user_id_two` int(11) NOT NULL COMMENT 'Second friend user id'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `guest_list`
--

CREATE TABLE `guest_list` (
  `user_id` int(11) NOT NULL COMMENT 'user_id who signed up',
  `dish_id` int(11) NOT NULL COMMENT 'dish_id for which dish the user signed for ',
  `special_request` varchar(500) NOT NULL COMMENT 'special requests'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL COMMENT 'Primary Key for Users',
  `username` varchar(20) NOT NULL COMMENT 'Username for User',
  `password` varchar(30) NOT NULL COMMENT 'Password for User - Hashed',
  `email` varchar(20) NOT NULL COMMENT 'Email for User',
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `address` varchar(60) NOT NULL,
  `city` varchar(20) NOT NULL,
  `state` varchar(2) NOT NULL,
  `zipcode` int(5) NOT NULL,
  `security_question` varchar(100) NOT NULL,
  `security_answer` varchar(100) NOT NULL,
  `user_permission` int(11) NOT NULL DEFAULT '0' COMMENT '0 - Regular User, 1 - Admin',
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'When the user was created',
  `is_active` int(11) NOT NULL DEFAULT '1' COMMENT 'Did the user deactivate/Delete their account',
  `profile_picture` varchar(30) NOT NULL COMMENT 'Path to profile picture',
  `location` varchar(30) NOT NULL COMMENT 'long and lat',
  `user_rating` double NOT NULL COMMENT 'Averaged rating'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `password`, `email`, `first_name`, `last_name`, `address`, `city`, `state`, `zipcode`, `security_question`, `security_answer`, `user_permission`, `date_created`, `is_active`, `profile_picture`, `location`, `user_rating`) VALUES
(1, 'adayal', 'password', 'adayal@vt.edu', 'Amit', 'Dayal', '123 fake street', 'Blacksburg', 'VA', 24060, 'What is your favorite color?', 'Hot Pink', 0, '2017-11-21 12:53:50', 1, 'default.png', '-77.4310990,38.8942790', 3);

-- --------------------------------------------------------

--
-- Table structure for table `votes`
--

CREATE TABLE `votes` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT 'user_id who voted - Foreign Key',
  `action` int(11) NOT NULL COMMENT '-1 downvoted, 1 upvoted',
  `comments_id` int(11) NOT NULL COMMENT 'Comments table entry - Foreign Key',
  `dish_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `comments_vote_id` (`vote_id`),
  ADD KEY `comments_user_id` (`user_id`),
  ADD KEY `comment_table_id` (`comment_table_id`),
  ADD KEY `vote_id` (`vote_id`);

--
-- Indexes for table `comments_conjunction`
--
ALTER TABLE `comments_conjunction`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cuisine_type`
--
ALTER TABLE `cuisine_type`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dish`
--
ALTER TABLE `dish`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `dish_comment_id` (`comments_id`),
  ADD KEY `dish_user_id` (`user_id`) USING BTREE,
  ADD KEY `dish_cuisine_id` (`cuisine_id`);

--
-- Indexes for table `friends`
--
ALTER TABLE `friends`
  ADD KEY `user_id_one` (`user_id_one`),
  ADD KEY `user_id_two` (`user_id_two`) USING BTREE;

--
-- Indexes for table `guest_list`
--
ALTER TABLE `guest_list`
  ADD KEY `guest_list_user_id` (`user_id`),
  ADD KEY `guest_list_dish_id` (`dish_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `votes`
--
ALTER TABLE `votes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `votes_comments_id` (`comments_id`),
  ADD KEY `votes_user_id` (`user_id`),
  ADD KEY `dish_id` (`dish_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `comments_conjunction`
--
ALTER TABLE `comments_conjunction`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `cuisine_type`
--
ALTER TABLE `cuisine_type`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `dish`
--
ALTER TABLE `dish`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary Key for Users', AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `votes`
--
ALTER TABLE `votes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `comments_ibfk_3` FOREIGN KEY (`comment_table_id`) REFERENCES `comments_conjunction` (`id`);

--
-- Constraints for table `dish`
--
ALTER TABLE `dish`
  ADD CONSTRAINT `dish_ibfk_2` FOREIGN KEY (`cuisine_id`) REFERENCES `cuisine_type` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `dish_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `dish_ibfk_4` FOREIGN KEY (`comments_id`) REFERENCES `comments_conjunction` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `friends`
--
ALTER TABLE `friends`
  ADD CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`user_id_one`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`user_id_two`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `guest_list`
--
ALTER TABLE `guest_list`
  ADD CONSTRAINT `guest_list_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `guest_list_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `votes`
--
ALTER TABLE `votes`
  ADD CONSTRAINT `votes_ibfk_3` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
