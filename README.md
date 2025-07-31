# PROG7314-ICE-Task1

## Team Members: ##
ST10284732 - George Bekker

## :page_facing_up: Project Documentation: ##
### Integration Process ###
- API's Used: TheMealDB API

### Why This API: ###

- Free and easy to use

- Provides detailed meal data including name, image, category, ingredients, and recipe instructions perfect for a student food/recipe app

## :blue_book: Technical Steps: ##

### API Request Setup: ###

- Used "https://www.themealdb.com/api/json/v1/1/categories.php" to fetch meal categories.

- Used "www.themealdb.com/api/json/v1/1/filter.php?c=MealCategory" to fetch all meals with that category.

- Used "https://www.themealdb.com/api/json/v1/1/search.php?s=MealName" to search for meals and get recipe details.

### JSON Parsing: ###

- Used AsyncTask to make network calls and parse the JSON response.

### Displaying Data: ###

- Used RecyclerView to show categories and meals.

- Used Glide to load meal images from URLs.

- Created Intent to pass selected meal info to a detail page showing the recipe.

### UI: ###

- Custom layout files (XML) for each screen.

- Simple navigation using intents and onClickListeners.

## :iphone: User Guide ##
### How to Use the Student Meal App ###
1. Launch the App
- Opens the home screen with a list of meal categories.

2. Browse Meals
- Tap on a category to see all meals in that category.

3. View Recipes
- Tap on a meal to view the full recipe including ingredients, instructions, and image.

4. Navigation
- Use the Back button to return to the previous screen.
