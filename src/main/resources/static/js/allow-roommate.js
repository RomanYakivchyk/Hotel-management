  var arrivalDatePicker, leaveDatePicker, arrivalDateConfig, leaveDateConfig;
        arrivalDateConfig = {
            locale: 'ru-ru',
            format: 'dd/mm/yyyy',
            uiLibrary: 'bootstrap4',
            keyboardNavigation: true,
            modal: true,
            header: true,
            footer: true,
            maxDate: function() {
                var arr1 = $('#leaveDate').val().split('/');
                var leaDate = new Date(parseInt(arr1[2]), parseInt(arr1[1]) - 1, parseInt(arr1[0]));
                var leaveDateMinus1 = moment(leaDate).subtract(1, 'days');
                return leaveDateMinus1;
            }
        };
        leaveDateConfig = {
            locale: 'ru-ru',
            format: 'dd/mm/yyyy',
            uiLibrary: 'bootstrap4',
            keyboardNavigation: true,
            modal: true,
            header: true,
            footer: true,
            minDate: function() {
                var arr2 = $('#arrivalDate').val().split('/');
                var arrDate = new Date(parseInt(arr2[2]), parseInt(arr2[1]) - 1, parseInt(arr2[0]));
                var arrDatePlus1 = moment(arrDate).add(1, 'days');
                return arrDatePlus1;
            }
        };
        $(document).ready(function() {
            $('select').selectpicker();
            arrivalDatePicker = $('#arrivalDate').datepicker(arrivalDateConfig);
            leaveDatePicker = $('#leaveDate').datepicker(leaveDateConfig);
        });


        var roomNameNumberMap = {
            "Кімната 1": 1,
            "Кімната 2": 2,
            "Кімната 3": 3,
            "Кімната 4": 4,
            "2-й поверх": 5,
            "Кімната 5": 6,
            "Кімната 6": 7,
            "Кімната 7": 8,
            "Кімната 8": 9
        };



        $(document).ready(function() {

            initialize();

            observeRoomSelect();

            $('#allowBringIn').click(function() {
                if (!$(this).is(':checked')) {
                    actionsAfterUncheck();
                } else if ($(this).is(':checked')) {
                    actionsAfterCheck();
                }
            });

        });

        function actionsAfterCheck() {
            var $bringInRoomSelect = $('#BringInRoomsSelect');
            var title = $('*[data-id="rooms"]').attr("title");
            var roomNameArray = title.split(', ');
            $('#BringInRoomsSelect option').each(function() {
                if (jQuery.inArray($(this).text(), roomNameArray) !== -1) {
                    $(this).attr("disabled", false);
                } else {
                    $(this).attr("disabled", true);
                }
            });
            $bringInRoomSelect.selectpicker('refresh');
            $bringInRoomSelect.parent().parent().css("display", "block");
        }

        function actionsAfterUncheck() {
            var $bringInRoomSelect = $('#BringInRoomsSelect');
            $('#BringInRoomsSelect option').each(function() {
                $(this).prop("selected", false);
                $(this).attr("disabled", true);
            });
            $bringInRoomSelect.selectpicker('refresh');
            $bringInRoomSelect.parent().parent().css("display", "none");
        }


        function observeRoomSelect() {

            var targetNode = $('*[data-id="rooms"]').get(0);
            var config = {
                attributes: true,
                childList: false,
                subtree: false,
                attributeFilter: ["title"]
            };

            var callback = function(mutationsList, observer) {
                var $checkbox = $('#allowBringIn');

                var noRoomSelected = $('*[data-id="rooms"]').attr("title").split(', ')[0] === "Вибрати кімнати";
                if ($checkbox.is(':checked')) {
                    $($checkbox).prop('checked', false);
                    actionsAfterUncheck();
                }
                if (noRoomSelected) {
                    $checkbox.attr("disabled", true);
                } else {
                    $checkbox.attr("disabled", false);
                }
            };
            var observer = new MutationObserver(callback);
            observer.observe(targetNode, config);
        }

        function initialize() {
            var noRoomSelected = $('*[data-id="rooms"]').attr("title").split(', ')[0] === "Вибрати кімнати";
            var $bringInRoomSelect = $('#BringInRoomsSelect');
            if (!noRoomSelected) {
                if ($('#allowBringIn').prop('checked')) {
                    $('#allowBringIn').attr("disabled", false);
                    actionsAfterCheck();
                } else {
                    actionsAfterUncheck();
                }
            } else {
                $('#allowBringIn').attr("disabled", true);
                actionsAfterUncheck();
            }
        }