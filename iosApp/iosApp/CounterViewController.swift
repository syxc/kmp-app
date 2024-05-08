//
//  CounterViewController.swift
//  iosApp
//
//  Created by syxc on 2024/5/9.
//  Copyright © 2024 github.com. All rights reserved.
//

import Foundation
import SharedKit
import UIKit

class CounterViewController: UIViewController {
  private var delegate: CounterViewControllerDelegate?

  override func viewDidLoad() {
    super.viewDidLoad()
    let container = UIStackView()
    container.axis = .horizontal
    container.alignment = .fill
    container.distribution = .fillEqually
    container.translatesAutoresizingMaskIntoConstraints = false

    view.addSubview(container)
    container.leadingAnchor.constraint(equalTo: view.leadingAnchor).isActive = true
    container.topAnchor.constraint(equalTo: view.topAnchor).isActive = true
    container.widthAnchor.constraint(equalTo: view.widthAnchor).isActive = true
    container.heightAnchor.constraint(equalTo: view.heightAnchor).isActive = true

    self.delegate = CounterViewControllerDelegate(root: container)
  }

  // MARK: - deinit

  deinit {
    delegate?.dispose()
  }
}
