# Project 3 - *EnzoGram*

**EnzoGram** is a photo sharing app similar to Instagram but using Parse as its backend.

Time spent: **5** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can view the last 20 posts submitted to "Instagram".
- [x] The user should switch between different tabs - viewing all posts (feed view), compose (capture photos form camera) and profile tabs (posts made) using fragments and a Bottom Navigation View. (2 points)
- [x] User can pull to refresh the last 20 posts submitted to "Instagram".

The following **optional** features are implemented:

- [x] User sees app icon in home screen and styled bottom navigation view
- [x] Style the feed to look like the real Instagram feed.
- [ ] User can load more posts once he or she reaches the bottom of the feed using infinite scrolling.
- [x] Show the username and creation time for each post.
- [x] User can tap a post to view post details, including timestamp and caption.
- [x] User Profiles
      - [x] Allow the logged in user to add a profile photo
      - [x] Display the profile photo with each post
      - [ ] Tapping on a post's username or profile photo goes to that user's profile page and shows a grid view of the user's posts 
- [ ] User can comment on a post and see all comments for each post in the post details screen.
- [ ] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:

- [x] Placeholder icons in the top right of the toolbar for the implementation of future features

## Video Walkthrough

Here's a walkthrough of implemented user stories:

Part Two
<img src='https://github.com/enzofalone/EnzoGram/blob/main/enzogrampreview3.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Here we can see the first part of this project including the application customized icon in the home screen
<img src='https://github.com/enzofalone/EnzoGram/blob/main/enzogrampreview.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

I usually try to do all the stretch stories as they are really interesting to motivate myself to dig into more documentation and learn more about the libraries used and Android but I could not develop likes and comments due to lack of time and my few tries did not work for the features to be pushed into the project. It is my first time working with databases, and even though I had a lot of fun and felt really excited, it is at the moment something hard that I will definitely improve on as I really liked going through it.

I had problems creating and updating user profiles for a while but after reading documentation I quickly encountered the solution, which was to save in background after calling a put.

## Open-source libraries used

- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [2022] [Enzo Falone]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
