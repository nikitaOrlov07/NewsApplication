News Application (SpringBoot Application)

# Features for Non-Authenticated Users

- Search for news articles by category, language, or publication date (combinations are possible)
- Search for news articles by title
- View news article details (detail page)
- Non-authenticated users can view news , but news view count will not increase
- Non-authenticated users can authenticate or register
- Pagination is available for convenient news browsing

# Features for Authenticated Users

In addition to the features for non-authenticated users, authenticated users can:
- Like or dislike news articles
- Write, edit, and delete comments
- Like or dislike comments
- When an authenticated user views a news article, the news view count will increase only for the first visit; subsequent visits will not affect the view count
- Personal cabinet to view recently viewed news articles, liked news articles, and recent comments
- Authenticated users can delete their account

# Admin Features

- Admin users can perform all actions available to authenticated users, plus:
- Create, update, and delete news articles
- Delete user accounts
- Search for users by username (implemented on the cabinet page using fragment logic)

# Project Details

- The project is primarily written in Java, i also used HTML, CSS, and JavaScript for the front-end
- Page navigation logic for selecting news articles is primarily implemented in JavaScript
- JUnit and Mockito tests are included
- Also i used PostgreSQL database for this project.
## Screenshots

### Main Page
![Main Page](https://github.com/nikitaOrlov07/NewsApplication/assets/145924436/bc8287af-146a-4a41-82c8-e797d46194c7)
*Browse the latest news articles with easy search and navigation.*

### Article Detail Page
![Detail Page](https://github.com/nikitaOrlov07/NewsApplication/assets/145924436/37fc0287-fb7c-448b-a1c4-0ec78b693590)
*Read full articles with rich content and images.*

### User Cabinet
![User Cabinet](https://github.com/nikitaOrlov07/NewsApplication/assets/145924436/384824a2-18e0-4d6d-a0b4-9e7adf73ced8)
*Track your reading habits and manage your profile.*

### Login and Register pages 
![image](https://github.com/nikitaOrlov07/NewsApplication/assets/145924436/372ce1aa-c4a1-4a47-86d0-0c43a140e4c3)
*you can log in or create a new account. * 

### ADMIN can delete , create and update news
![image](https://github.com/nikitaOrlov07/NewsApplication/assets/145924436/c1144014-38c3-4ea5-a4ce-b6865908a135)


Enjoy exploring the project!
