# Project 4 - *Parstagram*

**Parstagram** is a photo sharing app using Parse as its backend.

Time spent: **18** hours spent in total

## User Stories

The following **required** functionality is completed:

- [X] User sees app icon in home screen.
- [X] User can sign up to create a new account using Parse authentication
- [X] User can log in and log out of his or her account
- [X] The current signed in user is persisted across app restarts
- [X] User can take a photo, add a caption, and post it to "Instagram"
- [X] User can view the last 20 posts submitted to "Instagram"
- [X] User can pull to refresh the last 20 posts submitted to "Instagram"
- [X] User can tap a post to view post details, including timestamp and caption.

The following **stretch** features are implemented:

- [X] Style the login page to look like the real Instagram login page.
- [X] Style the feed to look like the real Instagram feed.
- [X] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using fragments and a Bottom Navigation View.
- [X] User can load more posts once he or she reaches the bottom of the feed using endless scrolling.
- [X] Show the username and creation time for each post
- [X] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
  - [ ] Allow the logged in user to add a profile photo
  - [ ] Display the profile photo with each post
  - [X] Tapping on a post's username or profile photo goes to that user's profile page
  - [X] User Profile shows posts in a grid view
- [ ] User can comment on a post and see all comments for each post in the post details screen.
- [X] User can like a post and see number of likes for each post in the post details screen.

The following **additional** features are implemented:

- [X] Settings page with logout button
- [X] Additional sign up page with password confirmation
- [X] Indeterminate progress bar for login page.

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. Optimal way to implement likes in order to avoid race conditions and improve efficiency
2. Best practice way to find the size of a Relation within Parse for things like comments, likes, and follows
3. Strategies for hashing passwords or making them more secure in some way
4. How to get HTTPS image URLs from the Parse backend rather than HTTP

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='./walkthrough.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [Kap](https://getkap.co).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library


## Notes

There were a lot of technical challenges I had to overcome to create this app, such as implementing Fragments and FragmentManagers, syncing Fragment and Activity navigation, forming proper ParseQuerys, using a FileProvider to save image files, and more. I had to learn a lot of new material to complete this project, and had to learn it at a much faster rate than previous projects. The challenges were not only conceptual and technical, but also organizational. I originally had ProfileFragment as a subclass of HomeFragment, but when trying to implement the profile grid view I realized that it would be a huge mess to try to maintain the inheritance, as I would have had to
- define my own abstract subclass of RecyclerView.Adapter so that both PostsAdapter and PostsGridAdapter could implement the methods addAll() and clear()
- refactor the usages of the adapter within HomeFragment to use that subclass, but I couldn't replace every single one and would need to cast the adapter to PostsAdapter in certain instances
- override the inner class PostsAdapter.ViewHolder within PostsGridAdapter, but there isn't a way to override inner classes within Java

It was very frustrating for me that the classes were so similar and would lend themselves to an inheritance relationship but the high complexity of the situation made it impossible to do so in an elegant way. This app was challenge not only of my ability to implement functionality but to organize my codebase on a larger scale.

## License

    Copyright [yyyy] [name of copyright owner]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
