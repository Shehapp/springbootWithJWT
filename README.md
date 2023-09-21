## Secure the endpoint with JWT



### to authenticate 
- /api/v1/auth/register...........................POST..........with body {username, password, role}
- /api/v1/auth/authenticate...................POST .........with body {username, password}
- /api/v1/auth/refresh-token/{token}.....POST 
- /api/v1/auth/logout.............................GET ...........with header Authorization: Bearer {token}



### we have only two endpoints to secure it: 
-   /api/v1/users...............GET........ with header Authorization: Bearer {token}
-  /api/v1/users/{id}........GET........ with header Authorization: Bearer {token}
 
### we response  access_token(jwtToken) and refresh_token(token)
  - access_token will be saved in client only and valid for 15 min
      and have claims (username, role, exp)
  - refresh_token will be saved in server and client and valid for 30 days.

### pros
  - query the database when the access_token is expired only ~ 15 min.
  - if access_token has been stolen, it will be valid for 15 min only.
  - if refresh_token has been stolen, user can logout to invalidate the refresh_token.

### cons
  - if user logout, the refresh_token will be invalidated but the access_token will be valid for maximum 15 min.