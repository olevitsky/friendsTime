1. Test check in mechanism
2. ParseLoginUI DOC: https://github.com/ParsePlatform/ParseUI-Android
3/10/2015 Added Parse notification when new event is added. For now it just displays Toast. For debugging purposes it always sends message to the originating device
  See createEventActivity.java
      // !! For debugging purposes. For production, uncomment line below and comment one after!!!
            //pQuery.whereEqualTo("username", contact.getFirstEmail().getAddress());
            pQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

  To Do: Update notification: 
    - call a different acitivity to ask user to review/accept invite and then update user calendar
    - add a way to send msg/comment/new proposal back to the sender (will need to have sender uname in the message)
To do: (general)
  - YELP queries
  - Add GEO based search (search around me or certain place on the map within certain radius)
