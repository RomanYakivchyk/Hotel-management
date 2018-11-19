 $(document).ready(function() {

            $("#newClientSubmitButton").click(function(event) {

                var valid = true;

                var newClientName = $('#newClientName').val();
                var otherClientInfo = $('#otherClientInfo').val();
                var phoneNumber = $('#phoneNumber').val();
                var email = $('#email').val();

                if (!newClientName) {
                    $('#clientNameError').css("display", "block");
                    $('#newClientName').addClass("is-invalid");
                    valid = false;
                } else {
                    $('#clientNameError').css("display", "none");
                    $('#newClientName').removeClass("is-invalid");
                }

                if (!otherClientInfo) {
                    $('#otherClientInfoError').css("display", "block");
                    $('#otherClientInfo').addClass("is-invalid");
                    valid = false;
                } else {
                    $('#otherClientInfoError').css("display", "none");
                    $('#otherClientInfo').removeClass("is-invalid");
                }

                if (!phoneNumber) {
                    $('#phoneNumberError').css("display", "block");
                    $('#phoneNumber').addClass("is-invalid");
                    valid = false;
                } else {
                    $('#phoneNumberError').css("display", "none");
                    $('#phoneNumber').removeClass("is-invalid");
                }

                var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/

                if (email) {
                    if (!email.match(re)) {
                        $('#emailError').css("display", "block");
                        $('#email').addClass("is-invalid");
                        valid = false;
                    } else {
                        $('#emailError').css("display", "none");
                        $('#email').removeClass("is-invalid");
                    }
                } else {
                        $('#emailError').css("display", "none");
                        $('#email').removeClass("is-invalid");
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
//                        url: 'https://quiet-springs-81500.herokuapp.com/clients/create-ajax', // action attribute of form
                        url: 'http://localhost:8080/clients/create-ajax', // action attribute of form
                        dataType: "json",
                        data: clientObject,
                        success: function(data) {
                            $('#clientName').append('<option value="' + data[0] + '">' + data[1] + '</option>');
                            $('#clientName').val(data[0]);
                            $('#clientName').selectpicker('refresh');
                            clearModal();
                            $('#addClassModal').modal('hide');
                        }
                    });
                }
            });


            $('#closeClientModal').click(function(){
                clearModal();
            });
 });


 function clearModal(){
     $('#clientNameError').css("display", "none");
     $('#newClientName').removeClass("is-invalid");
     $('#newClientName').val('');

      $('#otherClientInfoError').css("display", "none");
      $('#otherClientInfo').removeClass("is-invalid");
      $('#otherClientInfo').val('');

      $('#phoneNumberError').css("display", "none");
      $('#phoneNumber').removeClass("is-invalid");
      $('#phoneNumber').val('');

      $('#emailError').css("display", "none");
      $('#email').val('');

 }