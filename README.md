# 📈 Bitcoin Price Tracker 💰

This is a simple and visual Android app that tracks real-time and historical Bitcoin prices using **Jetpack Compose**. It includes animated graphs, filter buttons, and fallback error handling — all structured with modern Android development practices.

## :camera_flash: Screenshots
<p float="left">
  <img src="https://github.com/alanliongar/BitcoinPrice/blob/master/Screenshots/Screenshot_01.png" width="250"/> 
  <img src="https://github.com/alanliongar/BitcoinPrice/blob/master/Screenshots/Screenshot_02.png" width="250"/>
  <img src="https://github.com/alanliongar/BitcoinPrice/blob/master/Screenshots/Screenshot_03.png" width="250"/>
</p>

## ✨ Features

- 🪙 **Live Bitcoin Price** — Updated from public API
- 📊 **Historical Price Chart** — With animation and filters (1D, 7D, 1M, etc.)
- 🎛️ **Filter Buttons** — Built with LazyHorizontalGrid for fluid UX
- 🔁 **Automatic UI refresh** with `LaunchedEffect` and coroutines
- ❌ **Error State Handling** — Graceful fallback and user feedback when API fails
- 🎨 **Color-coded Price Display** — Green when rising, Red when falling
- ⚙️ **Custom animation using Compose Charts**

## 📚 Libraries Used

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Retrofit](https://square.github.io/retrofit/)
- [Compose Charts (by @ehsannarmani)](https://github.com/ehsannarmani/Compose-Charts)  
  `implementation("io.github.ehsannarmani:compose-charts:0.1.7")`

## 🧠 What I Learned

- Creating dynamic and responsive UIs with Compose
- Handling multiple API calls with isolated error states
- Visualizing data with animated charts
- Managing UI state using `remember`, `mutableStateOf`, and `LaunchedEffect`

## License
```
The MIT License (MIT)

Copyright (c) 2025 Alan Lucindo Gomes

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
