## Decompose LazyList

Using [Decompose](https://github.com/arkivanov/Decompose) with compose LazyList

Visual logic of the list item lifecycle

<img src="documentation/img.png" alt="drawing" height="400"/>

The index of an item between the first visible item inclusive and the last visible item inclusive is `Status.RESUMED`, the item before the first
visible item and after the last visible item is `Status.STARTED`, the two items after `Status.STARTED` have the status `Status.CREATED`, and the rest
have the status `Status.DESTROYED`.  If no element is visible, the first element in the list will be in the `Status.CREATED` status.

![Example](documentation/Screen_recording_20241214_102602.gif)
