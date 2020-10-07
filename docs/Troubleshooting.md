# Troubleshooting

This document is reserved for any common issues we might run into with our dev environments, Git, etc.

## Dependency Issues

I ran into these a lot early on and they drove me nuts until I set this option; the issues seem to have gone away, I recommend everyone do the following in their IntelliJ ASAP:

1. File -> Settings
2. Build, Execution, Deployment -> Build Tools -> Maven
3. Make sure **Always update snapshots** is checked.

![](./images/maven_build_settings.png)