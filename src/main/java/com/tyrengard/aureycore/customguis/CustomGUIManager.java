package com.tyrengard.aureycore.customguis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("rawtypes")
public final class CustomGUIManager implements Listener {
	private static final ArrayList<CustomGUIItem> customGUIItems = new ArrayList<>();
	private static final HashMap<UUID, ACustomGUI> customGUIs = new HashMap<>();
	
	public static void openGUI(ACustomGUI gui, Player p) {
		p.closeInventory();
		customGUIs.put(p.getUniqueId(), gui);
		gui.assignPlayer(p);
		p.openInventory(gui.getInventory());
		gui.onOpen(p);
	}
	
	static void registerCustomGUIItem(CustomGUIItem item) {
		customGUIItems.add(item);
	}
    
    @SuppressWarnings("unchecked")
	@EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
		Inventory clickedInventory = e.getClickedInventory();
		if (clickedInventory == null) return;
		
		InventoryHolder topHolder = e.getView().getTopInventory().getHolder();
		
		// if top inventory is a custom GUI
		if (topHolder instanceof ACustomGUI gui && customGUIs.containsValue(gui)) {
			if (gui instanceof ACustomStoringGUI storingGUI) {
				if (storingGUI.isStorageLocked())
					e.setCancelled(true);
				else {
					storingGUI.updateStorageInventory();
//				ItemStack cursorItem = e.getCursor();
//				int slot = e.getSlot();
//
//				// if cursor has item
//				if (cursorItem != null && cursorItem.getType() != Material.AIR) {
//					// if slot has item
//					if (item != null) {
//						// remove it from storage
//						storingGUI.removeItemFromStorage(item);
//						// if shift click
//						if (e.isShiftClick()) {
//							// add removed item to player inventory
//							e.getWhoClicked().getInventory().addItem(item);
//						}
//						// if left click
//						else if (e.isLeftClick()) {
//							// if items are the same
//							if (item.isSimilar(cursorItem)) {
//								// add them together
//								int combinedAmount = item.getAmount() + cursorItem.getAmount(),
//									newSlotAmount = Math.min(cursorItem.getMaxStackSize(), combinedAmount),
//									excessAmount = combinedAmount - newSlotAmount;
//								ItemStack combinedItems = cursorItem.clone(), excessItems = cursorItem.clone();
//								combinedItems.setAmount(newSlotAmount);
//								excessItems.setAmount(excessAmount);
//								storingGUI.setItemInStorage(slot, combinedItems);
//								e.getWhoClicked().setItemOnCursor(excessItems);
//							}
//							// if items are different
//							else {
//								// set cursor to removed item and set slot to cursor item
//								storingGUI.setItemInStorage(slot, cursorItem);
//								e.getWhoClicked().setItemOnCursor(item);
//							}
//						}
//						// if right click
//						else if (e.isRightClick()) {
//							// if items are the same
//							if (item.isSimilar(cursorItem)) {
//								// if slot can fit more
//								if (item.getAmount() < item.getMaxStackSize()) {
//									// add one from cursor
//									int newSlotAmount = item.getAmount() + 1,
//										excessAmount = cursorItem.getAmount() - 1;
//									ItemStack combinedItems = cursorItem.clone(), excessItems = cursorItem.clone();
//									combinedItems.setAmount(newSlotAmount);
//									excessItems.setAmount(excessAmount);
//									storingGUI.setItemInStorage(slot, combinedItems);
//									e.getWhoClicked().setItemOnCursor(excessItems);
//								}
//								// else ignore it
//							}
//							// if items are different
//							else {
//								// set cursor to removed item and set slot to cursor item
//								storingGUI.setItemInStorage(slot, cursorItem);
//								e.getWhoClicked().setItemOnCursor(item);
//							}
//						}
//					}
//
//					// if slot is empty
//					else {
//						// if left click
//						if (e.isLeftClick()) {
//							// transfer whole stack from cursor to slot
//							storingGUI.setItemInStorage(slot, cursorItem);
//							e.getWhoClicked().setItemOnCursor(null);
//						}
//						// if right click
//						else if (e.isRightClick()) {
//							// add one to storage and remove one from cursor
//							ItemStack oneOfCursorItem = cursorItem.clone();
//							oneOfCursorItem.setAmount(1);
//							storingGUI.setItemInStorage(slot, oneOfCursorItem);
//							cursorItem.setAmount(cursorItem.getAmount() - 1);
//						}
//					}
//				}
//				// if cursor is empty but slot is not empty
//				else if (item != null) {
//					// if right click
//					if (e.isRightClick()) {
//						// transfer half of stack in slot to cursor
//						ItemStack newCursorItem = item.clone();
//						item.setAmount(item.getAmount() / 2); // divide slot items in two
//						newCursorItem.setAmount(newCursorItem.getAmount() - item.getAmount()); // remove slot items from new cursor item
//						storingGUI.setItemInStorage(slot, item); // remove new cursor items from inventory
//						e.getWhoClicked().setItemOnCursor(newCursorItem);
//					} else {
//						storingGUI.removeItemFromStorage(item);
//						// if shift click
//						if (e.isShiftClick()) {
//							// transfer whole stack from slot to player inventory
//							e.getWhoClicked().getInventory().addItem(item);
//						}
//						// if left click
//						else if (e.isLeftClick()) {
//							// transfer whole stack from slot to cursor
//							e.getWhoClicked().setItemOnCursor(item);
//						}
//					}
//				}
				}
			}

			// click happened inside GUI
			if (topHolder.getInventory().equals(clickedInventory)) {
				if (gui instanceof ItemRestricting itemRestrictingSpecialGUI) {
					switch (e.getAction()) {
						case PLACE_ALL: case PLACE_ONE: case PLACE_SOME:
							if (!itemRestrictingSpecialGUI.slotAllowsItemStack(e.getSlot(), e.getCursor())) {
								e.setCancelled(true);
								return;
							}
							break;
						case HOTBAR_SWAP:
							if (!itemRestrictingSpecialGUI.slotAllowsItemStack(e.getSlot(), e.getWhoClicked().getInventory().getItem(e.getHotbarButton()))) {
								e.setCancelled(true);
								return;
							}
							break;
					}
				}

				ItemStack item = e.getCurrentItem();

				// check if button is a custom GUI item
				for(CustomGUIItem customGUIItem : CustomGUIManager.customGUIItems)
					if (customGUIItem.isSimilar(item)) {
						e.setCancelled(true);

						switch (e.getClick()) {
							case LEFT: case RIGHT: case SHIFT_LEFT: case SHIFT_RIGHT:
								customGUIItem.onClick(gui, e);
								break;
							default: break;
						}
						return;
					}

				// check if GUI storage
				if (gui instanceof ACustomStoringGUI storingGUI)
					if (storingGUI.isStorageLocked())
						e.setCancelled(true);
					else {
						storingGUI.updateStorageInventory();

//				ItemStack cursorItem = e.getCursor();
//				int slot = e.getSlot();
//
//				// if cursor has item
//				if (cursorItem != null && cursorItem.getType() != Material.AIR) {
//					// if slot has item
//					if (item != null) {
//						// remove it from storage
//						storingGUI.removeItemFromStorage(item);
//						// if shift click
//						if (e.isShiftClick()) {
//							// add removed item to player inventory
//							e.getWhoClicked().getInventory().addItem(item);
//						}
//						// if left click
//						else if (e.isLeftClick()) {
//							// if items are the same
//							if (item.isSimilar(cursorItem)) {
//								// add them together
//								int combinedAmount = item.getAmount() + cursorItem.getAmount(),
//									newSlotAmount = Math.min(cursorItem.getMaxStackSize(), combinedAmount),
//									excessAmount = combinedAmount - newSlotAmount;
//								ItemStack combinedItems = cursorItem.clone(), excessItems = cursorItem.clone();
//								combinedItems.setAmount(newSlotAmount);
//								excessItems.setAmount(excessAmount);
//								storingGUI.setItemInStorage(slot, combinedItems);
//								e.getWhoClicked().setItemOnCursor(excessItems);
//							}
//							// if items are different
//							else {
//								// set cursor to removed item and set slot to cursor item
//								storingGUI.setItemInStorage(slot, cursorItem);
//								e.getWhoClicked().setItemOnCursor(item);
//							}
//						}
//						// if right click
//						else if (e.isRightClick()) {
//							// if items are the same
//							if (item.isSimilar(cursorItem)) {
//								// if slot can fit more
//								if (item.getAmount() < item.getMaxStackSize()) {
//									// add one from cursor
//									int newSlotAmount = item.getAmount() + 1,
//										excessAmount = cursorItem.getAmount() - 1;
//									ItemStack combinedItems = cursorItem.clone(), excessItems = cursorItem.clone();
//									combinedItems.setAmount(newSlotAmount);
//									excessItems.setAmount(excessAmount);
//									storingGUI.setItemInStorage(slot, combinedItems);
//									e.getWhoClicked().setItemOnCursor(excessItems);
//								}
//								// else ignore it
//							}
//							// if items are different
//							else {
//								// set cursor to removed item and set slot to cursor item
//								storingGUI.setItemInStorage(slot, cursorItem);
//								e.getWhoClicked().setItemOnCursor(item);
//							}
//						}
//					}
//
//					// if slot is empty
//					else {
//						// if left click
//						if (e.isLeftClick()) {
//							// transfer whole stack from cursor to slot
//							storingGUI.setItemInStorage(slot, cursorItem);
//							e.getWhoClicked().setItemOnCursor(null);
//						}
//						// if right click
//						else if (e.isRightClick()) {
//							// add one to storage and remove one from cursor
//							ItemStack oneOfCursorItem = cursorItem.clone();
//							oneOfCursorItem.setAmount(1);
//							storingGUI.setItemInStorage(slot, oneOfCursorItem);
//							cursorItem.setAmount(cursorItem.getAmount() - 1);
//						}
//					}
//				}
//				// if cursor is empty but slot is not empty
//				else if (item != null) {
//					// if right click
//					if (e.isRightClick()) {
//						// transfer half of stack in slot to cursor
//						ItemStack newCursorItem = item.clone();
//						item.setAmount(item.getAmount() / 2); // divide slot items in two
//						newCursorItem.setAmount(newCursorItem.getAmount() - item.getAmount()); // remove slot items from new cursor item
//						storingGUI.setItemInStorage(slot, item); // remove new cursor items from inventory
//						e.getWhoClicked().setItemOnCursor(newCursorItem);
//					} else {
//						storingGUI.removeItemFromStorage(item);
//						// if shift click
//						if (e.isShiftClick()) {
//							// transfer whole stack from slot to player inventory
//							e.getWhoClicked().getInventory().addItem(item);
//						}
//						// if left click
//						else if (e.isLeftClick()) {
//							// transfer whole stack from slot to cursor
//							e.getWhoClicked().setItemOnCursor(item);
//						}
//					}
//				}
					}
			}

			// click happened outside of GUI
			else {
				if (gui instanceof ItemRestricting itemRestrictingSpecialGUI) {
					switch (e.getAction()) {
						case MOVE_TO_OTHER_INVENTORY:
							if (!itemRestrictingSpecialGUI.slotAllowsItemStack(e.getSlot(), e.getCurrentItem())) {
								e.setCancelled(true);
								return;
							}
							break;
					}
				}
				if (gui instanceof ACustomStoringGUI storingGUI) {
					if (storingGUI.getLockedPlayerInventorySlot() == e.getSlot())
						e.setCancelled(true);
					else
						storingGUI.updateStorageInventory();
				}
			}
		}
	}
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
    	Inventory inv = e.getInventory();
    	InventoryHolder holder = inv.getHolder();
    	
    	if (customGUIs.remove(e.getPlayer().getUniqueId()) != null) {
    		ACustomGUI gui = (ACustomGUI) holder;
    		gui.onClose((Player) e.getPlayer());
    	}
    }

//    private ClickOutcome[] getClickOutcomes(InventoryClickEvent e) {
//		PlayerInventory playerInventory = e.getWhoClicked().getInventory();
//		Inventory clickedInventory = e.getClickedInventory(), unclickedInventory = clickedInventory == playerInventory ? e.getInventory();
//		int clickedSlot = e.getSlot();
//		ItemStack itemInCursor = e.getCursor(), itemClicked = e.getCurrentItem();
//
//		ClickOutcome SWAP_WITH_CURSOR_OUTCOME = new ClickOutcome(ClickAction.SWAP_WITH_CURSOR, itemInCursor, clickedInventory, clickedSlot, itemClicked);
//		ClickOutcome[] outcomes;
//		switch (e.getClick()) {
//			case LEFT:
//				if (itemInCursor == null)
//					return new ClickOutcome[] { new ClickOutcome(ClickAction.PICKUP, null, clickedInventory, clickedSlot, itemClicked) };
//				else if (itemClicked == null)
//					return new ClickOutcome[] { new ClickOutcome(ClickAction.PLACE, itemInCursor, clickedInventory, clickedSlot, null) };
//				else if (itemInCursor.isSimilar(itemClicked)) {
//					ItemStack[] combinedItems = ItemUtils.combine(itemInCursor, itemClicked);
//					if (combinedItems == null)
//						return null;
//					else
//						return new ClickOutcome[] { new ClickOutcome(ClickAction.COMBINE, combinedItems[0], clickedInventory, clickedSlot, combinedItems[1]) };
//				} else
//					return new ClickOutcome[] { SWAP_WITH_CURSOR_OUTCOME };
//				break;
//			case RIGHT:
//				if (itemInCursor == null)
//					if (itemClicked == null)
//						return new ClickOutcome[] { new ClickOutcome(ClickAction.NONE, null, clickedInventory, clickedSlot, null) };
//					else if (itemClicked.getAmount() == 1)
//						return new ClickOutcome[] { new ClickOutcome(ClickAction.PICKUP, null, clickedInventory, clickedSlot, itemClicked) };
//					else {
//						int amountLeft = itemClicked.getAmount() / 2, amountPickedUp = itemClicked.getAmount() - amountLeft;
//						ItemStack itemStackPickedUp = itemClicked.clone(), itemStackLeft = itemClicked.clone();
//						itemStackPickedUp.setAmount(amountPickedUp);
//						itemStackLeft.setAmount(amountLeft);
//						return new ClickOutcome[] { new ClickOutcome(ClickAction.PICKUP_HALF, itemStackLeft, clickedInventory, clickedSlot, itemStackPickedUp) };
//					}
//				else if (itemClicked == null)
//					if (itemInCursor.getAmount() == 1)
//						return new ClickOutcome[] { new ClickOutcome(ClickAction.PLACE, itemInCursor, clickedInventory, clickedSlot, null) };
//					else {
//						int amountLeft = itemInCursor.getAmount() - 1, amountPlaced = 1;
//						ItemStack itemStackPlaced = itemInCursor.clone(), itemStackLeft = itemInCursor.clone();
//						itemStackPlaced.setAmount(amountPlaced);
//						itemStackLeft.setAmount(amountLeft);
//						return new ClickOutcome[] { new ClickOutcome(ClickAction.PLACE_ONE, itemStackPlaced, clickedInventory, clickedSlot, itemStackLeft) };
//					}
//				else if (itemInCursor.isSimilar(itemClicked)) {
//					if (itemClicked.getAmount() < itemClicked.getMaxStackSize()) {
//						int amountLeft = itemInCursor.getAmount() - 1, amountCombined = itemClicked.getAmount() + 1;
//						ItemStack itemStackCombined = itemClicked.clone(), itemStackLeft = itemInCursor.clone();
//						itemStackCombined.setAmount(amountCombined);
//						itemStackLeft.setAmount(amountLeft);
//						return new ClickOutcome[] { new ClickOutcome(ClickAction.COMBINE_ONE, itemStackCombined, clickedInventory, clickedSlot, itemStackLeft)};
//					} else
//						return new ClickOutcome[] { new ClickOutcome(ClickAction.NONE, itemClicked, clickedInventory, clickedSlot, itemInCursor)};
//				} else
//					return new ClickOutcome[] { SWAP_WITH_CURSOR_OUTCOME };
//				break;
//			case SHIFT_LEFT: case SHIFT_RIGHT:
//				if (itemClicked != null)
//					return new ClickOutcome[] { new ClickOutcome(ClickAction.TRANSFER, itemClicked, clickedInventory, clickedSlot, itemInCursor)};
//					clickAction = ClickAction.TRANSFER;
//				else
//					clickAction = ClickAction.NONE;
//				break;
//			case DROP:
//				if (itemInCursor != null)
//					clickAction = ClickAction.DROP;
//				else
//					clickAction = ClickAction.NONE;
//				break;
//			case NUMBER_KEY:
//				if (itemClicked != null || playerInventory.getItem(e.getHotbarButton()) != null)
//					clickAction = ClickAction.SWAP_WITH_HOTBAR;
//				else
//					clickAction = ClickAction.NONE;
//				break;
//			case SWAP_OFFHAND:
//				if (itemClicked != null || playerInventory.getItemInOffHand().getType() != Material.AIR)
//					clickAction = ClickAction.SWAP_WITH_OFFHAND;
//				else
//					clickAction = ClickAction.NONE;
//				break;
//			case CONTROL_DROP:
//				if (itemClicked != null || playerInventory.getItem(e.getHotbarButton()) != null)
//					clickAction = ClickAction.DROP_STACK;
//				else
//					clickAction = ClickAction.NONE;
//				break;
//			case WINDOW_BORDER_RIGHT:
//				clickAction = ClickAction.DROP;
//				break;
//			case WINDOW_BORDER_LEFT:
//				clickAction = ClickAction.DROP_STACK;
//				break;
//			case DOUBLE_CLICK:
//			case UNKNOWN:
//			case CREATIVE:
//			case MIDDLE:
//				break;
//		}
//	}
}
