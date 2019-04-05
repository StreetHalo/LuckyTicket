# Lucky Ticket

### About
App can check your ticket's numer on the good luck. <br />
You can input number with keyboard or select an area with a number and just make photo. <br />
After then app calculate ticket's numer and show you answer.<br />

This app using MVP arcitecture pattern and android.hardware.Camera.

### Implementations
* [Cropper](https://github.com/edmodo/cropper) - for select an area with a ticket's number;
* [Tesseract](https://github.com/rmtheis/tess-two) - OCR library for read number from cropped image.

For work with views, async tasks and dependency injections:<br />
* Butterknife, RxJava and Dagger 2.

### Remark
If your ticket number not clear , maybe you can get problem with speed and reading numbers.

### Screenshots
<p float="left">
  <img src="https://github.com/StreetHalo/LuckyTicket/blob/master/app/src/main/res/drawable/view_3.jpg" width="250" />
  <img src="https://github.com/StreetHalo/LuckyTicket/blob/master/app/src/main/res/drawable/view_2.jpg" width="250" /> 
  <img src="https://github.com/StreetHalo/LuckyTicket/blob/master/app/src/main/res/drawable/view_1.jpg" width="250" />
</p>
