# extension-legacy-api

An extension for LuckPerms v5, which implements the legacy v4 API.

This allows plugins coded against previous versions of LuckPerms to continue to operate with the newer versions.

This is not a long term solution, it exists merely to reduce some of the update burden on users. Developers are encouraged to update to the new v5 API as soon as possible.


### Event listening
Only a limited number of events are able to be listened to.

Currently supported:

* `GroupCacheLoadEvent`
* `GroupDataRecalculateEvent`
* `NodeAddEvent`
* `NodeClearEvent`
* `NodeMutateEvent`
* `NodeRemoveEvent`
* `UserCacheLoadEvent`
* `UserDataRecalculateEvent`
* `UserDemoteEvent`
* `UserFirstLoginEvent`
* `UserLoadEvent`
* `UserPromoteEvent`
* `UserTrackEvent`
