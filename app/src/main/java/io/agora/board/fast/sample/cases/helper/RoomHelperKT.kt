package io.agora.board.fast.sample.cases.helper

import io.agora.board.fast.FastboardView

object RoomHelperKT {
    /**
     * 注入样式，禁止页码输入框交互与选中。
     */
    fun injectSlideNumberInputDisableStyle(fastboardView: FastboardView) {
        val js = """
            (function() {
                var styleId = 'netless-number-input-disable-style';
                if (!document.getElementById(styleId)) {
                    var style = document.createElement('style');
                    style.id = styleId;
                    style.innerHTML = `
                        .netless-app-slide-page-number-input {
                            pointer-events: none !important;
                            user-select: none !important;
                        }
                    `;
                    document.head.appendChild(style);
                }
            })();
        """.trimIndent()
        fastboardView.whiteboardView.evaluateJavascript(js)
    }

    /**
     * 移除样式，恢复页码输入框交互。
     */
    fun removeSlideNumberInputDisableStyle(fastboardView: FastboardView) {
        val js = """
            (function() {
                var style = document.getElementById('netless-number-input-disable-style');
                if (style && style.parentNode) {
                    style.parentNode.removeChild(style);
                }
            })();
        """.trimIndent()
        fastboardView.whiteboardView.evaluateJavascript(js)
    }

    fun injectSlideControl(fastboardView: FastboardView) {
        val js = """
        (function() {
          if (window.NetlessSlideControl) return;
          window.NetlessSlideControl = {
            pageInputDisable: function() {
              var input = document.querySelector('.netless-app-slide-page-number-input');
              if (input) {
                input.setAttribute('tabindex', '-1');
                input.blur && input.blur();
                input.style.pointerEvents = 'none';
              }
            },
            pageInputEnable: function() {
              var input = document.querySelector('.netless-app-slide-page-number-input');
              if (input) {
                input.removeAttribute('tabindex');
                input.style.pointerEvents = 'auto';
              }
            },
            footerDisable: function() {
              var footer = document.querySelector('.netless-app-slide-footer');
              if (footer) {
                footer.style.pointerEvents = 'none';
              }
            },
            footerEnable: function() {
              var footer = document.querySelector('.netless-app-slide-footer');
              if (footer) {
                footer.style.pointerEvents = 'auto';
              }
            }
          };
        })();
    """.trimIndent()

        fastboardView.whiteboardView.evaluateJavascript(js, null)
    }

}