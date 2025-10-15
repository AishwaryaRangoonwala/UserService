Github Create Repo Command: 
gh repo create UserService --public --source=. --remote=origin --push

CREATE DATABASE userservice;
CREATE USER userservice_admin;
GRANT ALL PRIVILEGES ON userservice.* TO userservice_admin;