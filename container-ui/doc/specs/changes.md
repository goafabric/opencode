# sideview
- the sideview is a little to wide, can you narrow it because the longest text for now is container, there should still be some spacing to the right view
- can you give the sideview a possibilty to change the wideness via dragging

# log view
- while scrolling works we have a scrollbar at the outer pane and the log pane which is weird
- it would be good that the outer pane will just always fit the view without a scrollbar
- but the inner log view should still have a log view

# log view search
- can you also add to the top a search box with a magnifier icon, that only display the lines of log matching the search
- it should be interactive, so whenever typing it should start changig the log result

# container view
- can you give the container view an auto refresh, so that when new containers are loaded they will be shown (after 1,2 seconds)
- you then also have a way to deal with the cpu and mem that are loaded dynamic

# image view
- deleting an image does nothing, it will refresh the view but the image is still there
- now that you are using container cmd you can do "container image rm <image>"

# volume view
- deleting a volume does nothing, it will refresh the view but the volume is still there
- now that you are using container cmd you can do "container volume rm <volume>"