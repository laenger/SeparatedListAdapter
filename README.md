# SeparatedListAdapter

## Basic usage

```java
SeparatedListAdapter separatedListAdapter = new SeparatedListAdapter(this, R.layout.item_header, R.id.item_header_text, R.id.item_divider);

separatedListAdapter.addSection("section 1", new DemoAdapter1(this, Arrays.asList(42, 4711, 7)));
separatedListAdapter.addSection("section 2", new DemoAdapter2(this, Arrays.asList("foo", "bar")));
separatedListAdapter.addSection(null,        new DemoAdapter3(this, Arrays.asList("lorem", "ipsum", "dolor")));
separatedListAdapter.addSection("section 4", new DemoAdapter1(this, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)));

setListAdapter(separatedListAdapter);
```

## Include in your project

Add the maven repo url to your `build.gradle`:

```groovy
repositories {
    maven { url "https://raw.github.com/laenger/maven-releases/master/releases" }
}
```

Add the library to the dependencies:

```groovy
dependencies {
    compile "biz.laenger.android:SeparatedListAdapter:1.0.0"
}
```
