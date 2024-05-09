//
//  MokoView.swift
//  iosApp
//
//  Created by syxc on 2024/5/9.
//  Copyright © 2024 github.com. All rights reserved.
//

import SharedKit
import SwiftUI

struct MokoView: View {
  var body: some View {
    VStack(spacing: 16) {
      Image(systemName: "swift")
        .font(.system(size: 200))
        .foregroundColor(.accentColor)
      Text("Moko Reousce: \(IOSPlatform().name)")
    }
  }
}

#Preview {
  MokoView()
}
