 $(document).ready(function() {

            $("#newClientSubmitButton").click(function(event) {

                var valid = true;

                var newClientName = $('#newClientName').val();
                var otherClientInfo = $('#otherClientInfo').val();
                var phoneNumber = $('#phoneNumber').val();
                var email = $('#email').val();

                if (!newClientName) {
                    $('#clientNameError').css("display", "block");
                    valid = false;
                } else {
                    $('#ClientNameError').css("display", "none");
                }

                if (!otherClientInfo) {
                    $('#otherClientInfoError').css("display", "block");
                    valid = false;
                } else {
                    $('#otherClientInfoError').css("display", "none");
                }

                if (!phoneNumber) {
                    $('#phoneNumberError').css("display", "block");
                    valid = false;
                } else {
                    $('#phoneNumberError').css("display", "none");
                }

                var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/

                if (email) {
                    if (!email.match(re)) {
                        $('#emailError').css("display", "block");
                        valid = false;
                    } else {
                        $('#emailError').css("display", "none");
                    }
                }

                if (valid) {

                    var clientObject = {
                        "id": "",
                        "inactive": false,
                        "name": newClientName,
                        "otherClientInfo": otherClientInfo,
                        "email": email,
                        "phoneNumber": phoneNumber
                    };

                    var url = $('#clientFormModal').data('href');
                    $.ajax({
                        type: 'post', // method attribute of form
                        url: url, // action attribute of form
                        dataType: "json",
                        data: clientObject,
                        success: function(data) {
                            $('#clientName').append('<option value="' + data[0] + '">' + data[1] + '</option>');
                            $('#clientName').val(data[0]);
                            $('#clientName').selectpicker('refresh');
                            $('#addClassModal').modal('hide');
                        }
                    });
                }
            });
 });