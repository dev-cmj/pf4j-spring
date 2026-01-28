package com.project.core.extension;

import org.pf4j.ExtensionPoint;

public interface UiExtensionPoint extends ExtensionPoint {
    String getSlotId();      // 버튼이 들어갈 위치 (예: "vdi-list-toolbar")
    String getButtonHtml();  // 렌더링할 HTML 코드
    boolean isEnabled();     // ON/OFF 상태
}