$().ready(function () {
    $('#quotes').change(changedQuotes);
    $('#submitBtn').click(clickedSubmitButton);
});

function changedQuotes() {
    const param = {
        base: $('#base').val(),
        quotes: $('#quotes option:selected').val()
    }

    if (param.quotes === "") {
        $('#submitBtn').prop('disabled', true);
        $('#rate').text('');
        return false;
    }

    $.ajax({
        url: '/rate?' + $.param(param),
        success: function (data) {
            console.log(data);
            $('#submitBtn').prop('disabled', false);
            const rate = numberWithCommas(data.rate);
            const currency = data.quotedCurrency + '/' + data.baseCurrency;
            $('#rate').text(rate + ' ' + currency);
        }
    })
}


function clickedSubmitButton() {
    const param = {
        base: $('#base').val(),
        quotes: $('#quotes option:selected').val(),
        remittance: $('#remittance').val()
    }

    if (!$.isNumeric(param.remittance)) {
        $('#amount').css('color', 'red');
        $('#amount').text("송금액이 바르지 않습니다.");
        return false;
    }

    $.ajax({
        url: '/amount?' + $.param(param),
        success: function (data) {
            console.log(data)
            const exchangeRate = data.exchangeRate;
            const amount = numberWithCommas(data.amount.value);
            $('#amount').css('color', 'black');
            $('#amount').text(
                '수취금액은 '
                + amount
                + ' '
                + exchangeRate.quotedCurrency
                + ' 입니다.'
            );
        },
        error: function (error) {
            $('#amount').css('color', 'red');
            $('#amount').text(error.responseText);
        }
    })
}

function numberWithCommas(n) {
    const num = n.toFixed(2);
    return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}