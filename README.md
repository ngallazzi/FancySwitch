//[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-FancySwitch-green.svg?style=flat )]( https://android-arsenal.com/details/1/7413 )
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
<br>
# FancySwitch

A nice looking switch on/off button

<div>
  <img src="https://raw.githubusercontent.com/ngallazzi/FancySwitch/master/demo.gif" width="400" hspace="20" />
  <br/>
</div>

# Usage

in your build.gradle (Module)
```groovy
implementation 'com.github.ngallazzi:FancySwitch:master-SNAPSHOT'
```

in your build.gradle (Project)
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
# In your .xml
```groovy
  <it.ngallazzi.fancyswitch.FancySwitch
            android:id="@+id/fancySwitch"
            android:layout_height="110dp"
            android:layout_width="wrap_content"
            app:orientation="PORTRAIT"
            app:actionOffDrawable="@drawable/ic_hdr_off"
            app:actionOnDrawable="@drawable/ic_hdr_on"
            app:baseColor="@android:color/holo_blue_dark" />
```

# In your activity/fragment
```groovy
// Kotlin
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
FancySwitch fancySwitch = findViewById(R.id.fancySwitch);
fancySwitch.setSwitchStateChangedListener(new FancySwitch.SwitchStateChangedListener() {
	@Override
	public void onChanged(@NotNull FancySwitch.State newState) {
		Toast.makeText(MainActivity.this, "New switch state: " + newState.name(), Toast.LENGTH_SHORT).show();
		}	
	});
}
```
```groovy
// Java 

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	FancySwitch fancySwitch = findViewById(R.id.fancySwitch);
	fancySwitch.setSwitchStateChangedListener(new FancySwitch.SwitchStateChangedListener() {
		@Override
		public void onChanged(@NotNull FancySwitch.State newState) {
			Toast.makeText(MainActivity.this, "New switch state: " + newState.name(), Toast.LENGTH_SHORT).show();
		}
	});
}
```


# Options
 - custom **orientation**: "app:orientation" - sets the orientation of the switch (PORTRAIT/LANDSCAPE)
 - custom **actionOffDrawable**: "app:actionOffDrawable" - sets the drawable desired for the OFF state
 - custom **actionOnDrawable**: "app:actionOnDrawable" - sets the drawable desired for the ON state
 - custom **baseColor**: "app:baseColor" - sets the base color for the switch 
 
# Public Methods

- setSwitchStateChangedListener(listener: SwitchStateChangedListener): sets a listener for switch state changes
 
 # License
```groovy 
Copyright 2019 Nicola Gallazzi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
