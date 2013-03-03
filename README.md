
# Pull Request Overview

Karter is a web app for viewing your organisations Github pull requests.

## Usage

To start Karter just set some env vars and run it through Leiningen.

```
export KARTER_AUTH="user:password"
export KARTER_USER="organisation_or_user_name"
export KARTER_PORT="3456"

lein run
```

The auth var is optional, and the user should be the name of the
user or organisation you want to view.  You'll then be able to
browse the site at:

http://localhost:3456

![](http://github.com/rodnaph/karter/raw/master/docs/pulls.png)

## Inventor

The idea for this project was from [@jenkoian](https://github.com/jenkoian).

