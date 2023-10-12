# Minecraft Ultimate Login 2 plugin

- UltimateLogin 2.0.0
- Target Version: 1.20+
- Author: DrachenClon22
- Supported Languages: English, Russian

Updates from version 1:
- Accounts data doesn't divide to rows in accounts file, now it's raw data all in 1 line only;
- Changed hash method to SHA-256;
- Added attempts for users who have used a wrong password on login command;
- Added multiple configs in config file;
- Added different language support;
- Added logout command;

Features:
- Accounts file doesn't contain any readable information. If someone can compromise your server files, they won't even get nicknames, not even the administration, not even me.<br /> Example of raw user account file: `u8JxG/HRwVKyGwDNrfifpjfHYjj/yHGxfhu1ypkIoQQ=iyf3gqHFNI2Jcs55zUuUv5NpPQz3cDE05QGNoiGAi14=R0rMCu0qXk/NJm+JZe3wlJ7sLbyobjSRajZt5eO5m4Q=EsoXtJryKJQ28wPgFmAwoh5SXSZuIJJnQzgBqP1AcaA=00`;
- Protection against file modification. If someone modified accounts file, remain info still usable by plugin;
- User information saved inside file, so no need to enter password every time they join, only if suspicious activity spotted;
- If user ran of of their attempts, user gets permanently banned, until admins manually pardon them.

Minecraft bukkit plugin for user authorization in cases:
1. Need additional protection for players or administration even on online servers;
2. Offline servers;
3. Other unknown for me reasons.

Version 1 was made and used only by me and my friends, so it's never listed anywhere, but still this is version 2.

Basic commands:
- `/register <password> <password>` - used for registration on the server, it is recommended to remember the password, because you do not have to enter it every time you logging on the server;
- `/login <password>` - used in cases of suspicious activities in user account, or if password has been changed by admins;
- `/password change <from> <to> <to>` - used for updating the user's password by user;
- `/password logout` - used for logging out of the system for current user.

Admin commands:
- `/password user <user> set <to>` - sets password for specific user;
- `/password user <user> delete` - remove user from database.

Permissions:
```MiniYAML
login.basic:
     description: Allows to use the basic commands
     default: true
login.*:
     description: Get access to all UltimateLogin commands
     default: op
     children:
       login.basics: true
       login.set.*: true
       login.set.other: true
       login.set.admin: true
       login.delete.users: true
       login.delete.admin: true
login.delete.users:
     description: Allows to delete passwords for users
     default: op
login.delete.admin:
     description: Allows to delete passwords for admins
     default: op
     children:
       login.delete.users: true
login.set.*:
     description: Allows to use all of the set commands
     default: op
     children:
       login.set.other: true
       login.set.admin: true
login.set.admin:
     description: Allows to use all of the set commands for players and admins
     default: op
     children:
       login.set.other: true
login.set.other:
     description: Allows to use all of the set commands for players
     default: op
```

Links:
- [Spigot Plugin Page](https://www.spigotmc.org/resources/ultimate-login.112969/)
