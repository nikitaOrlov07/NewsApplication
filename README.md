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

Enjoy exploring the project!

# Some photos
- Main page
![image](https://github.com/nikitaOrlov07/NewsApplication/assets/145924436/81f473b3-2cb1-4d96-b562-4dcd3893c796)

-detail page 
![image](https://github.com/nikitaOrlov07/NewsApplication/assets/145924436/de81b3f1-5fd3-4d7f-858e-845bafc457cf)

- cabinet

![image](https://github.com/nikitaOrlov07/NewsApplication/assets/145924436/7b59470a-2d17-484c-888d-940383b13a4c)
