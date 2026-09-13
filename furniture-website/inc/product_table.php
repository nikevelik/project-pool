<!-- make table to preview products in checout, cart and order pages -->
<?php 
    $isCart = ('shop-backet'===$names_assoc_with_php_self[$_SERVER["PHP_SELF"]]);
?>
<table class="table text-nowrap">
            <thead>
            <tr class="text-sm">
                <th class="border-gray-300 border-top py-3" colspan="2">Product</th>
                <th class="border-gray-300 border-top py-3">Quantity</th>
                <th class="border-gray-300 border-top py-3">Unit price</th>
                <th class="border-gray-300 border-top py-3">Discount</th>
                <th class="border-gray-300 border-top py-3">Total</th>
                <th class="border-gray-300 border-top py-3"></th>
            </tr>
            </thead>
            <tbody>
                <?php for($i=0; $i<count($items); $i++){ $iter_id = $items[$i]['id'];?>
                    <tr class="text-sm" id = "row_cart_<?php echo $i; ?>">
                    <td class="align-middle border-gray-300 py-3"><a href="shop-detail.php?id=<?php echo $iter_id;?>"><img class="img-fluid flex-shrink-0" src="<?php echo $items[$i]['dir'].'/main.jpg';?>" alt="<?php echo $items[$i]['name'];?>" style="min-width: 50px" width="50"></a></td>
                    <td class="align-middle border-gray-300 py-3"><a href="shop-detail.php?id=<?php echo $iter_id;?>"><?php echo $items[$i]['name'];?></a></td>
                    <td class="align-middle border-gray-300 py-3">
                        <?php echo (!$isCart) ? $items[$i]['quantity']: ''?>
                        <?php if($isCart && !isset($_SESSION['in'])){ ?>
                         <input class = modify_quantity id = "_<?php echo $items[$i]['cookie']; ?>"class="form-control" type="number" value="<?php echo $items[$i]['quantity'];?>" style="max-width: 3.5rem">
                        <?php }else if($isCart && isset($_SESSION['in'])){ ?>
                            <input class = modify_quantity id = "_<?php echo $items[$i]['id']; ?>"class="form-control" type="number" value="<?php echo $items[$i]['quantity'];?>" style="max-width: 3.5rem">
                        <?php } ?>
                        </td>
                    <td class="align-middle border-gray-300 py-3">€<?php echo $items[$i]['price'];?></td>
                    <td class="align-middle border-gray-300 py-3">€0.00</td>
                    <td class="align-middle border-gray-300 py-3">€<?php echo $items[$i]['price']*$items[$i]['quantity'];?></td>
                    <?php if($isCart) { ?>
                        <td class="align-middle border-gray-300 py-3">
                            <button id = "btn_delete_row_cart_<?php echo $i; ?>" class="btn btn-link p-0" type="button"><i class="fas fa-trash-alt"></i></button>
                        </td>
                    <?php } ?>
                    </tr>
                <?php } ?>
            </tbody>
            <tfoot>
            <tr>
                <th class="py-3 border-0" colspan="5"> <span class="h5 text-gray-700 mb-0">Subtotal</span></th>
                <th class="py-3 border-0 text-end" colspan="2"> <span class="h5 text-gray-700 mb-0">€<?php echo $total; ?></span></th>
            </tr>
            <tr>
                <th class="py-3 border-0" colspan="5"> <span class="h5 text-gray-700 mb-0">Shipping</span></th>
                <th class="py-3 border-0 text-end" colspan="2"> <span class="h5 text-gray-700 mb-0">€<?php echo $shipping; ?></span></th>
            </tr>
            <tr>
                <th class="py-3 border-0" colspan="5"> <span class="h5 text-gray-700 mb-0">Total</span></th>
                <th class="py-3 border-0 text-end" colspan="2"> <span class="h5 text-gray-700 mb-0">€<?php echo $total+$shipping; ?></span></th>
            </tr>
            </tfoot>
</table>