/**
 * Created by aidenZou on 15/11/24.
 */
!function (w) {

    var ua = navigator.userAgent.toLowerCase();

    w.app = {
        //debug: true,
        debug: (-1 === location.search.indexOf("_debug")) ? false : true,
        // 是否移动端
        browser: {
            mobile: ua.match(/Mobile/i) !== null ? true : false,
            ios: ua.match(/iPhone|iPad|iPod/i) !== null ? true : false,
            android: ua.match(/Android/i) !== null ? true : false
        }
    };

    /**
     * 日志
     * @param message
     * @param data
     * @returns {boolean}
     */
    var uniqueId = 1;

    function log(message, data) {
        if (!app.debug) return false;

        console.log(data)

        var log = document.getElementById('log')
        var el = document.createElement('div')
        el.className = 'logLine'
        el.innerHTML = uniqueId++ + '. ' + message + ':<br/>' + JSON.stringify(data)
        if (log.children.length) {
            log.insertBefore(el, log.children[0])
        }
        else {
            log.appendChild(el)
        }
    }

    w.log = log;

    window.onerror = function (err) {
        log('window.onerror: ' + err)
    }
}(window);