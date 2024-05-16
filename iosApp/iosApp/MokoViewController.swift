//
//  MokoViewController.swift
//  iosApp
//
//  Created by syxc on 2024/5/9.
//  Copyright © 2024 github.com. All rights reserved.
//

import SwiftUI
import UIKit

class MokoViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()

        let vc = UIHostingController(rootView: MokoView())

        let mokoView = vc.view!
        mokoView.translatesAutoresizingMaskIntoConstraints = false

        addChild(vc)
        view.addSubview(mokoView)

        NSLayoutConstraint.activate([
            mokoView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            mokoView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            mokoView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            mokoView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
        ])

        vc.didMove(toParent: self)
    }
}
