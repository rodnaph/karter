
# Pull Request Overview

Karter is a web app for viewing your organisations Github pull requests.

## Usage

To start Karter just set some env vars and run it through Leiningen.

```
export KARTER_AUTH="user:password"
export KARTER_USER="organisation_or_user_name"

lein run
```

The auth var is optional, and the user should be the name of the
user or organisation you want to view.

